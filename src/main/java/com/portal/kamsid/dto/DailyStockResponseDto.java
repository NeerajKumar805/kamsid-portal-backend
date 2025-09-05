package com.portal.kamsid.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyStockResponseDto {
    private Long id;
    private LocalDate date;

    private Long productId;
    private String productName;

    private String billNo;
    private String remarks;
}
