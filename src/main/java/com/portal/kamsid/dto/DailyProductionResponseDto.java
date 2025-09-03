package com.portal.kamsid.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyProductionResponseDto {
    private Long id;
    private LocalDate date;
    private Long productId;
    private String productName;
    private String remarks;
}
