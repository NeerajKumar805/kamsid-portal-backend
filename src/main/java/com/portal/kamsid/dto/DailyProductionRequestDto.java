package com.portal.kamsid.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DailyProductionRequestDto {
    private Long id;
    @NotNull
    private LocalDate date;
    @NotEmpty
    private List<Long> productIds;
    private String remarks;
}
