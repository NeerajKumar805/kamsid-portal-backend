package com.portal.kamsid.service;

import com.portal.kamsid.dto.DailyStockRequestDto;
import com.portal.kamsid.dto.DailyStockResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface DailyStockService {
    List<DailyStockResponseDto> create(DailyStockRequestDto dto);

    List<DailyStockResponseDto> getAll();

    List<DailyStockResponseDto> getByDateRange(LocalDate start, LocalDate end);
}
