package com.portal.kamsid.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {

    @NotNull
    @JsonProperty("product_id")
    private Long productId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String colour;
    private String unit;

    private BigDecimal weight;
    private BigDecimal quantity;

    private String type;
    private String remark;
}
