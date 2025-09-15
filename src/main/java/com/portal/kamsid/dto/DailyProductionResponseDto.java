package com.portal.kamsid.dto;

import com.portal.kamsid.entity.Product;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyProductionResponseDto {
    private Long id;
    private LocalDate date;
    private String remarks;
    private ProductResponseDto product;
}
