package com.portal.kamsid.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.kamsid.dto.DailyMasterRequestDto;
import com.portal.kamsid.dto.DailyMasterResponseDto;
import com.portal.kamsid.dto.ProductRequestDto;
import com.portal.kamsid.entity.DailySaleMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.entity.ProductDetails;
import com.portal.kamsid.repository.DailySaleRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.DailySaleService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DailySaleServiceImpl implements DailySaleService {

	private final DailySaleRepository dailySaleRepo;
	private final ProductRepository productRepo;

	@Override
    public List<DailyMasterResponseDto> create(DailyMasterRequestDto dto) {
        List<ProductRequestDto> productDtos = Optional.ofNullable(dto.getProducts()).orElseGet(ArrayList::new);
        if (productDtos.isEmpty()) throw new IllegalArgumentException("products must not be empty");

        List<Long> ids = productDtos.stream().map(ProductRequestDto::getProductId).filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());
        if (ids.isEmpty()) throw new IllegalArgumentException("product_id must not be empty in products");

        List<Product> products = productRepo.findAllById(ids);
        Set<Long> found = products.stream().map(Product::getPid).collect(Collectors.toSet());
        List<Long> missing = ids.stream().filter(id -> !found.contains(id)).collect(Collectors.toList());
        if (!missing.isEmpty()) throw new IllegalArgumentException("Products not found: " + missing);

        Map<Long, Product> idToProduct = products.stream()
                .collect(Collectors.toMap(Product::getPid, Function.identity(), (a,b)->a));

        LocalDate masterDate = LocalDate.now(ZoneId.of("Asia/Kolkata"));

        DailySaleMaster master = DailySaleMaster.builder().date(masterDate).remark(dto.getRemark()).build();

        for (ProductRequestDto pr : productDtos) {
            Product product = idToProduct.get(pr.getProductId());
            LocalDate rowDate = pr.getDate() != null ? pr.getDate() : masterDate;

            ProductDetails pd = ProductDetails.builder()
                    .product(product).date(rowDate).colour(pr.getColour())
                    .type(pr.getType()).unit(pr.getUnit()).weight(pr.getWeight())
                    .quantity(pr.getQuantity()).remark(pr.getRemark()).billNo(pr.getBillNo()).build();

            // Important: use master.addProductDetails -> this will set saleMaster in ProductDetails
            master.addProductDetails(pd);
        }

        DailySaleMaster saved = dailySaleRepo.save(master);

        return saved.getProducts().stream().map(pd -> toDto(saved, pd, "SALE")).collect(Collectors.toList());
    }

    private DailyMasterResponseDto toDto(DailySaleMaster d, ProductDetails pd, String module) {
        Product p = pd.getProduct();
        return DailyMasterResponseDto.builder()
                .id(d.getId()).date(pd.getDate()).masterRemark(d.getRemark())
                .moduleType(module)
                .productId(p.getPid()).productName(p.getProductName())
                .productDetailsId(pd.getPdId()).type(pd.getType()).colour(pd.getColour())
                .unit(pd.getUnit()).weight(pd.getWeight()).quantity(pd.getQuantity())
                .productRemark(pd.getRemark()).billNo(pd.getBillNo()).build();
    }

	@Override
	public List<DailyMasterResponseDto> getAll() {
		return dailySaleRepo.findAll().stream().flatMap(d -> d.getProducts().stream().map(pd -> toDto(d, pd, "SALE")))
				.collect(Collectors.toList());
	}

	@Override
	public List<DailyMasterResponseDto> getByDateRange(LocalDate start, LocalDate end) {
		return dailySaleRepo.findByDateBetween(start, end).stream()
				.flatMap(d -> d.getProducts().stream().map(pd -> toDto(d, pd, "SALE"))).collect(Collectors.toList());
	}
}
