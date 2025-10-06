package com.portal.kamsid.service;

import com.portal.kamsid.dto.DailyProductionRequestDto;
import com.portal.kamsid.dto.DailyProductionResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface DailyProductionService {
    DailyProductionResponseDto create(DailyProductionRequestDto dto);
    List<DailyProductionResponseDto> createMany(DailyProductionRequestDto dto);
    List<DailyProductionResponseDto> getAll();
    List<DailyProductionResponseDto> getByDateRange(LocalDate start, LocalDate end);
}
