package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.DailyStockRequestDto;
import com.portal.kamsid.dto.DailyStockResponseDto;
import com.portal.kamsid.entity.DailyStockMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.DailyStockRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.DailyStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DailyStockServiceImpl implements DailyStockService {

    private final DailyStockRepository dailyStockRepo;
    private final ProductRepository productRepo;

    public DailyStockServiceImpl(DailyStockRepository dailyStockRepo, ProductRepository productRepo) {
        this.dailyStockRepo = dailyStockRepo;
        this.productRepo = productRepo;
    }

    @Override
    public DailyStockResponseDto create(DailyStockRequestDto dto) {
        Product p = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));

        DailyStockMaster d = new DailyStockMaster();
        d.setDate(dto.getDate());
        d.setProduct(p);
        d.setBill_no(dto.getBillNo());
        d.setRemarks(dto.getRemarks());

        DailyStockMaster saved = dailyStockRepo.save(d);
        return toDto(saved);
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
        DailyStockResponseDto dto = new DailyStockResponseDto();
        dto.setId(d.getId());
        dto.setDate(d.getDate());
        dto.setProductId(d.getProduct().getId());
        dto.setProductName(d.getProduct().getProductName());
        dto.setBillNo(d.getBill_no());
        dto.setRemarks(d.getRemarks());
        return dto;
    }
}
