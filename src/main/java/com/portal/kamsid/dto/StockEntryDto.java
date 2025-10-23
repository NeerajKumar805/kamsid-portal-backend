package com.portal.kamsid.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class StockEntryDto {
	private Long id;
	private Long productId;
	private String productName;
	private BigDecimal quantity;
	private String direction;
	private String sourceModule;
	private Long referenceMasterId;
	private Long referenceProductDetailsId;
	private LocalDate entryDate;
	private String remark;
}
