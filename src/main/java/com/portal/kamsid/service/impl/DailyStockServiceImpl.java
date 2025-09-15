package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.DailyStockRequestDto;
import com.portal.kamsid.dto.DailyStockResponseDto;
import com.portal.kamsid.dto.ProductResponseDto;
import com.portal.kamsid.entity.DailyStockMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.DailyStockRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.DailyStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DailyStockServiceImpl implements DailyStockService {

    private final DailyStockRepository dailyStockRepo;
    private final ProductRepository productRepo;

    @Override
    public DailyStockResponseDto create(DailyStockRequestDto dto) {
        Product p = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));

        DailyStockMaster d = DailyStockMaster.builder()
                .date(dto.getDate())
                .bill_no(dto.getBillNo())
                .product(p)
                .remarks(dto.getRemarks())
                .build();

        return toDto(dailyStockRepo.save(d));
    }

    @Override
    public List<DailyStockResponseDto> getAll() {
        return dailyStockRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<DailyStockResponseDto> getByDateRange(LocalDate start, LocalDate end) {
        return dailyStockRepo.findByDateBetween(start, end).stream().map(this::toDto).collect(Collectors.toList());
    }

    private DailyStockResponseDto toDto(DailyStockMaster d) {
        return DailyStockResponseDto.builder()
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
