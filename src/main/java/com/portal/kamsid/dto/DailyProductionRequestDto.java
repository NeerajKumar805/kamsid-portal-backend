package com.portal.kamsid.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyProductionRequestDto {
    private Long id;
    @NotNull
    private LocalDate date;
    @NotNull
    private Long productId;
    private String remarks;
}
