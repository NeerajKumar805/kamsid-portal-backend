package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.DailyProductionRequestDto;
import com.portal.kamsid.dto.DailyProductionResponseDto;
import com.portal.kamsid.dto.ProductRequestDto;
import com.portal.kamsid.entity.DailyProductionMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.entity.ProductDetails;
import com.portal.kamsid.repository.DailyProductionRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.DailyProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DailyProductionServiceImpl implements DailyProductionService {

	private final DailyProductionRepository dailyRepo;
	private final ProductRepository productRepo;

	@Override
	public List<DailyProductionResponseDto> create(DailyProductionRequestDto dto) {
		// validate products list
		List<ProductRequestDto> productDtos = Optional.ofNullable(dto.getProducts()).orElseGet(ArrayList::new);
		if (productDtos.isEmpty()) {
			throw new IllegalArgumentException("products must not be empty");
		}

		// collect product ids
		List<Long> ids = productDtos.stream().map(ProductRequestDto::getProductId).filter(Objects::nonNull).distinct()
				.collect(Collectors.toList());

		if (ids.isEmpty()) {
			throw new IllegalArgumentException("product_id must not be empty in products");
		}

		// fetch products and validate existence
		List<Product> products = productRepo.findAllById(ids);
		Set<Long> found = products.stream().map(Product::getPid).collect(Collectors.toSet());
		List<Long> missing = ids.stream().filter(id -> !found.contains(id)).collect(Collectors.toList());
		if (!missing.isEmpty()) {
			throw new IllegalArgumentException("Products not found: " + missing);
		}

		Map<Long, Product> idToProduct = products.stream()
				.collect(Collectors.toMap(Product::getPid, Function.identity(), (a, b) -> a));

		// master date: use provided or today
		LocalDate masterDate = dto.getDate() != null ? dto.getDate() : LocalDate.now();

		// create master
		DailyProductionMaster master = DailyProductionMaster.builder().date(masterDate).remark(dto.getRemark()).build();

		// build product details for each product DTO
		for (ProductRequestDto pr : productDtos) {
			Long pid = pr.getProductId();
			Product product = idToProduct.get(pid);
			if (product == null) {
				throw new IllegalArgumentException("Product not found: " + pid);
			}

			LocalDate prodDate = pr.getDate() != null ? pr.getDate() : masterDate;

			ProductDetails pd = ProductDetails.builder().product(product).date(prodDate).colour(pr.getColour())
					.type(pr.getType()).unit(pr.getUnit()).weight(pr.getWeight()).quantity(pr.getQuantity())
					.remark(pr.getRemark()).build();

			master.addProductDetails(pd);
		}

		// save master (cascades ProductDetails)
		DailyProductionMaster saved = dailyRepo.save(master);

		// return flat list of response DTOs (one per product row)
		return saved.getProducts().stream().map(pd -> toDto(saved, pd)).collect(Collectors.toList());
	}

	@Override
	public List<DailyProductionResponseDto> getAll() {
		return dailyRepo.findAll().stream().flatMap(d -> d.getProducts().stream().map(pd -> toDto(d, pd)))
				.collect(Collectors.toList());
	}

	@Override
	public List<DailyProductionResponseDto> getByDateRange(LocalDate start, LocalDate end) {
		return dailyRepo.findByDateBetween(start, end).stream()
				.flatMap(d -> d.getProducts().stream().map(pd -> toDto(d, pd))).collect(Collectors.toList());
	}

	private DailyProductionResponseDto toDto(DailyProductionMaster d, ProductDetails pd) {
		Product p = pd.getProduct();
		return DailyProductionResponseDto.builder().id(d.getId()).date(pd.getDate()).masterRemark(d.getRemark())

				.productId(p.getPid()).productName(p.getProductName())

				.productDetailsId(pd.getPdId()).type(pd.getType()).colour(pd.getColour()).unit(pd.getUnit())
				.weight(pd.getWeight()).quantity(pd.getQuantity()).productRemark(pd.getRemark()).build();
	}

}
