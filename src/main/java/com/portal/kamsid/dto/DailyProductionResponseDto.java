package com.portal.kamsid.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DailyProductionResponseDto {

	private Long id;
	private LocalDate date;
	private String masterRemark;

	private Long productId;
	private String productName;

	private Long productDetailsId;
	private String type;
	private String colour;
	private String unit;
	private BigDecimal weight;
	private BigDecimal quantity;
	private String productRemark;
}
