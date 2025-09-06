package com.portal.kamsid.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    @NotBlank
    private String productName;
    private String colour;
    private String type;
    private String unit;
    private String weight;
    private String quantity;
}
