package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.DailySaleRequestDto;
import com.portal.kamsid.dto.DailySaleResponseDto;
import com.portal.kamsid.dto.ProductResponseDto;
import com.portal.kamsid.entity.DailySaleMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.DailySaleRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.DailySaleService;
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
public class DailySaleServiceImpl implements DailySaleService {

    private final DailySaleRepository dailySaleRepo;
    private final ProductRepository productRepo;

    @Override
    public List<DailySaleResponseDto> create(DailySaleRequestDto dto) {
        List<Long> ids = new ArrayList<>(new LinkedHashSet<>(dto.getProductIds()));

        List<Product> products = productRepo.findAllById(ids);

        Set<Long> found = products.stream().map(Product::getId).collect(Collectors.toSet());
        List<Long> missing = ids.stream().filter(id -> !found.contains(id)).toList();
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Products not found: " + missing);
        }

        Map<Long, Product> idToProduct = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity(), (a, b) -> a));

        // build entities
        List<DailySaleMaster> toSave = new ArrayList<>();
        for (Long pid : ids) {
            Product p = idToProduct.get(pid);
            DailySaleMaster sale = DailySaleMaster.builder()
                    .date(dto.getDate())
                    .bill_no(dto.getBillNo())
                    .product(p)
                    .remarks(dto.getRemarks())
                    .build();
            toSave.add(sale);
        }

        List<DailySaleMaster> saved = dailySaleRepo.saveAll(toSave);

        return saved.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DailySaleResponseDto> getAll() {
        return dailySaleRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DailySaleResponseDto> getByDateRange(LocalDate start, LocalDate end) {
        return dailySaleRepo.findByDateBetween(start, end).stream().map(this::toDto).collect(Collectors.toList());
    }

    private DailySaleResponseDto toDto(DailySaleMaster d) {
        return DailySaleResponseDto.builder()
                .id(d.getId())
                .date(d.getDate())
                .billNo(d.getBill_no())
                .remarks(d.getRemarks())
                .product(ProductResponseDto.builder()
                        .id(d.getProduct().getId())
                        .productName(d.getProduct().getProductName())
                        .type(d.getProduct().getType())
                        .colour(d.getProduct().getColour())
                        .unit(d.getProduct().getUnit())
                        .weight(d.getProduct().getWeight())
                        .quantity(d.getProduct().getQuantity())
                        .build()
                )
                .build();
    }
}
