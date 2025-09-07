package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.DailyProductionRequestDto;
import com.portal.kamsid.dto.DailyProductionResponseDto;
import com.portal.kamsid.entity.DailyProductionMaster;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.DailyProductionRepository;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.DailyProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DailyProductionServiceImpl implements DailyProductionService {

    private final DailyProductionRepository dailyRepo;
    private final ProductRepository productRepo;

    @Override
    public DailyProductionResponseDto create(DailyProductionRequestDto dto) {
        Product p = productRepo.findById(dto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));
        DailyProductionMaster d = DailyProductionMaster.builder()
                .date(dto.getDate())
                .product(p)
                .remarks(dto.getRemarks())
                .build();

        return toDto(dailyRepo.save(d));
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
                .productId(d.getProduct().getId())
                .productName(d.getProduct().getProductName())
                .remarks(d.getRemarks())
                .build();
    }
}
