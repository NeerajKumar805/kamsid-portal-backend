package com.portal.kamsid.service;

import java.time.LocalDate;
import java.util.List;

import com.portal.kamsid.dto.DailyMasterRequestDto;
import com.portal.kamsid.dto.DailyMasterResponseDto;

public interface DailyProductionService {
	List<DailyMasterResponseDto> create(DailyMasterRequestDto dto);

	List<DailyMasterResponseDto> getAll();

	List<DailyMasterResponseDto> getByDateRange(LocalDate start, LocalDate end);
}
