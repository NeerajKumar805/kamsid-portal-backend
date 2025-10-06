package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.DailyProductionRequestDto;
import com.portal.kamsid.dto.DailyProductionResponseDto;
import com.portal.kamsid.dto.ProductResponseDto;
import com.portal.kamsid.entity.DailyProductionMaster;
import com.portal.kamsid.entity.Product;
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
    @Transactional
    public List<DailyProductionResponseDto> createMany(DailyProductionRequestDto dto) {
        List<Long> ids = new ArrayList<>(new LinkedHashSet<>(dto.getProductIds()));
        List<Product> products = productRepo.findAllById(ids);

        Set<Long> found = products.stream().map(Product::getId).collect(Collectors.toSet());
        List<Long> missing = ids.stream().filter(id -> !found.contains(id)).toList();
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Products not found: " + missing);
        }

        Map<Long, Product> idToProduct = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity(), (a,b) -> a));

        List<DailyProductionMaster> toSave = new ArrayList<>();
        for (Long pid : ids) {
            Product p = idToProduct.get(pid);
            DailyProductionMaster d = DailyProductionMaster.builder()
                    .date(dto.getDate())
                    .product(p)
                    .remarks(dto.getRemarks())
                    .build();
            toSave.add(d);
        }

        List<DailyProductionMaster> saved = dailyRepo.saveAll(toSave);
        return saved.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DailyProductionResponseDto> getAll() {
        return dailyRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DailyProductionResponseDto> getByDateRange(LocalDate start, LocalDate end) {
        return dailyRepo.findByDateBetween(start, end).stream().map(this::toDto).collect(Collectors.toList());
    }

    private DailyProductionResponseDto toDto(DailyProductionMaster d) {
        return DailyProductionResponseDto.builder()
                .id(d.getId())
                .date(d.getDate())
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
