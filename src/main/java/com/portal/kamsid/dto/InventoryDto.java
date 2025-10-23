package com.portal.kamsid.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InventoryDto {
    private Long productId;
    private String productName;
    private BigDecimal quantity;
    private LocalDate lastUpdated;
}
