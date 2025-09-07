package com.portal.kamsid.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {
    private Long id;
    private String productName;
    private String colour;
    private String type;
    private String unit;
    private String weight;
    private String quantity;
}
