package com.portal.kamsid.service;

import java.time.LocalDate;
import java.util.List;

import com.portal.kamsid.dto.InventoryDto;
import com.portal.kamsid.dto.StockEntryDto;

public interface InventoryService {
    List<InventoryDto> listAll();
    InventoryDto getByProductId(Long productId);
    List<StockEntryDto> findEntries(Long productId, LocalDate start, LocalDate end, String module, String direction);
    void rebuildInventoryFromEntries(); // admin only
}
