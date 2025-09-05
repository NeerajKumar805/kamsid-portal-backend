package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.DailySaleRequestDto;
import com.portal.kamsid.dto.DailySaleResponseDto;
import com.portal.kamsid.entity.DailySaleMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.DailySaleRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.DailySaleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DailySaleServiceImpl implements DailySaleService {

    private final DailySaleRepository dailySaleRepo;
    private final ProductRepository productRepo;

    public DailySaleServiceImpl(DailySaleRepository dailySaleRepo, ProductRepository productRepo) {
        this.dailySaleRepo = dailySaleRepo;
        this.productRepo = productRepo;
    }

    @Override
    public DailySaleResponseDto create(DailySaleRequestDto dto) {
        Product p = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));

        DailySaleMaster d = new DailySaleMaster();
        d.setDate(dto.getDate());
        d.setProduct(p);
        d.setBill_no(dto.getBillNo());
        d.setRemarks(dto.getRemarks());

        DailySaleMaster saved = dailySaleRepo.save(d);
        return toDto(saved);
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
        DailySaleResponseDto dto = new DailySaleResponseDto();
        dto.setId(d.getId());
        dto.setDate(d.getDate());
        dto.setProductId(d.getProduct().getId());
        dto.setProductName(d.getProduct().getProductName());
        dto.setBillNo(d.getBill_no());
        dto.setRemarks(d.getRemarks());
        return dto;
    }
}
