package com.portal.kamsid.service;

import com.portal.kamsid.dto.DailySaleRequestDto;
import com.portal.kamsid.dto.DailySaleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface DailySaleService {
    List<DailySaleResponseDto> create(DailySaleRequestDto dto);

    List<DailySaleResponseDto> getAll();

    List<DailySaleResponseDto> getByDateRange(LocalDate start, LocalDate end);
}
