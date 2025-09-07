package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.DailySaleRequestDto;
import com.portal.kamsid.dto.DailySaleResponseDto;
import com.portal.kamsid.entity.DailySaleMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.DailySaleRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.DailySaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DailySaleServiceImpl implements DailySaleService {

    private final DailySaleRepository dailySaleRepo;
    private final ProductRepository productRepo;

    @Override
    public DailySaleResponseDto create(DailySaleRequestDto dto) {
        Product p = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));

        DailySaleMaster d = DailySaleMaster.builder()
                .date(dto.getDate())
                .bill_no(dto.getBillNo())
                .product(p)
                .remarks(dto.getRemarks())
                .build();

        return toDto(dailySaleRepo.save(d));
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
                .productId(d.getProduct().getId())
                .productName(d.getProduct().getProductName())
                .billNo(d.getBill_no())
                .remarks(d.getRemarks())
                .build();
    }
}
