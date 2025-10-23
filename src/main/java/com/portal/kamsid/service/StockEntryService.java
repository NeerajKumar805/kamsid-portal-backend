package com.portal.kamsid.service;

import java.time.LocalDate;
import java.util.List;

import com.portal.kamsid.dto.StockEntryDto;

public interface StockEntryService {
	List<StockEntryDto> find(Long productId, LocalDate start, LocalDate end, String module, String direction);
}
