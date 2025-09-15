package com.portal.kamsid.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DailySaleResponseDto {
    private Long id;
    private LocalDate date;
    private String billNo;
    private String remarks;
    private ProductResponseDto product;
}
