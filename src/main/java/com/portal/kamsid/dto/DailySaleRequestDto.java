package com.portal.kamsid.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailySaleRequestDto {
    private Long id; // optional for update cases

    @NotNull
    private LocalDate date;

    @NotNull
    private Long productId;

    private String billNo;
    private String remarks;
}
