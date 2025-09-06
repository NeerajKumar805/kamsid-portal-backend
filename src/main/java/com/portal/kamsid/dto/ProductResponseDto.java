package com.portal.kamsid.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String productName;
    private String colour;
    private String type;
    private String unit;
    private String weight;
    private String quantity;
}
