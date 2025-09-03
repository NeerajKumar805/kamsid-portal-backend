package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.DailyProductionRequestDto;
import com.portal.kamsid.dto.DailyProductionResponseDto;
import com.portal.kamsid.entity.DailyProductionMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.DailyProductionRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.DailyProductionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DailyProductionServiceImpl implements DailyProductionService {

    private final DailyProductionRepository dailyRepo;
    private final ProductRepository productRepo;

    public DailyProductionServiceImpl(DailyProductionRepository dailyRepo, ProductRepository productRepo) {
        this.dailyRepo = dailyRepo;
        this.productRepo = productRepo;
    }

    @Override
    public DailyProductionResponseDto create(DailyProductionRequestDto dto) {
        Product p = productRepo.findById(dto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));
        DailyProductionMaster d = new DailyProductionMaster();
        d.setDate(dto.getDate());
        d.setProduct(p);
        d.setRemarks(dto.getRemarks());
        DailyProductionMaster saved = dailyRepo.save(d);
        return toDto(saved);
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
        DailyProductionResponseDto dto = new DailyProductionResponseDto();
        dto.setId(d.getId());
        dto.setDate(d.getDate());
        dto.setProductId(d.getProduct().getId());
        dto.setProductName(d.getProduct().getProductName());
        dto.setRemarks(d.getRemarks());
        return dto;
    }
}
