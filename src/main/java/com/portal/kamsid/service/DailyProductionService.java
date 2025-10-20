package com.portal.kamsid.service;

import java.time.LocalDate;
import java.util.List;

import com.portal.kamsid.dto.DailyProductionRequestDto;
import com.portal.kamsid.dto.DailyProductionResponseDto;

public interface DailyProductionService {
	List<DailyProductionResponseDto> create(DailyProductionRequestDto dto);

	List<DailyProductionResponseDto> getAll();

	List<DailyProductionResponseDto> getByDateRange(LocalDate start, LocalDate end);
}
