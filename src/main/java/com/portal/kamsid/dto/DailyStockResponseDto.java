package com.portal.kamsid.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyStockResponseDto {
    private Long id;
    private LocalDate date;
    private String billNo;
    private String remarks;
    private ProductResponseDto product;
}
