package com.portal.kamsid.service.impl;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.kamsid.dto.InventoryDto;
import com.portal.kamsid.dto.StockEntryDto;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.entity.ProductInventory;
import com.portal.kamsid.entity.StockEntry;
import com.portal.kamsid.exception.ResourceNotFoundException;
import com.portal.kamsid.repository.ProductInventoryRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.repository.StockEntryRepository;
import com.portal.kamsid.service.InventoryService;
import com.portal.kamsid.service.StockEntryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

	private final ProductInventoryRepository inventoryRepo;
	private final ProductRepository productRepo;
	private final StockEntryRepository entryRepo;
	private final StockEntryService stockEntryService;

	@Override
	@Transactional(readOnly = true)
	public List<InventoryDto> listAll() {
		List<ProductInventory> all = inventoryRepo.findAll();
		return all.stream().map(this::toDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryDto getByProductId(Long productId) {
		ProductInventory inv = inventoryRepo.findByProduct_Pid(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product: " + productId));
		return toDto(inv);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StockEntryDto> findEntries(Long productId, java.time.LocalDate start, java.time.LocalDate end,
			String module, String direction) {
		// delegate to StockEntryService which uses Criteria API
		return stockEntryService.find(productId, start, end, module, direction);
	}

	@Override
	@Transactional
	public void rebuildInventoryFromEntries() {
		// This is simple, correct approach: aggregate entries and upsert inventory
		// rows.
		// For very large datasets you should implement this in DB (group by) or run in
		// batches.
		List<StockEntry> entries = entryRepo.findAll();

		Map<Long, BigDecimal> agg = new HashMap<>(); // productId -> net quantity (IN - OUT)

		for (StockEntry se : entries) {
			Long pid = se.getProduct().getPid();
			BigDecimal q = se.getQuantity() == null ? BigDecimal.ZERO : se.getQuantity();
			BigDecimal curr = agg.getOrDefault(pid, BigDecimal.ZERO);
			if ("IN".equalsIgnoreCase(se.getDirection().name())) {
				curr = curr.add(q);
			} else {
				curr = curr.subtract(q);
			}
			agg.put(pid, curr);
		}

		// Use product repository to ensure product exists
		for (Map.Entry<Long, BigDecimal> e : agg.entrySet()) {
			Long pid = e.getKey();
			BigDecimal qty = e.getValue();

			Optional<Product> pOpt = productRepo.findById(pid);
			if (pOpt.isEmpty()) {
				// skip unknown product ids if any (shouldn't happen)
				continue;
			}

			Product p = pOpt.get();
			Optional<ProductInventory> invOpt = inventoryRepo.findByProduct_Pid(pid);
			ProductInventory inv = invOpt.orElseGet(() -> ProductInventory.builder().product(p)
					.quantity(BigDecimal.ZERO).lastUpdated(java.time.LocalDate.now(ZoneId.of("Asia/Kolkata"))).build());

			inv.setQuantity(qty);
			inv.setLastUpdated(java.time.LocalDate.now(ZoneId.of("Asia/Kolkata")));
			inventoryRepo.save(inv);
		}
	}

	private InventoryDto toDto(ProductInventory inv) {
		InventoryDto dto = new InventoryDto();
		dto.setProductId(inv.getProduct().getPid());
		dto.setProductName(inv.getProduct().getProductName());
		dto.setQuantity(inv.getQuantity());
		dto.setLastUpdated(inv.getLastUpdated());
		return dto;
	}
}
