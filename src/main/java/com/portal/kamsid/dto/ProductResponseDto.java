package com.portal.kamsid.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

	private Long id;

	@NotBlank
	private String productName;
}
