package com.portal.kamsid.service;

import com.portal.kamsid.dto.DailyMasterRequestDto;
import com.portal.kamsid.dto.DailyMasterResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface DailyStockService {
    List<DailyMasterResponseDto> create(DailyMasterRequestDto dto);

    List<DailyMasterResponseDto> getAll();

    List<DailyMasterResponseDto> getByDateRange(LocalDate start, LocalDate end);
}
