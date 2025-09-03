package com.portal.kamsid.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String productName;
    private String description;
    private String category;
    private String weight;
    private String size;
}
