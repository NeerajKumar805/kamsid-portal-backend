package com.portal.kamsid.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyMasterRequestDto {

	@NotEmpty
	@Valid
	private List<ProductRequestDto> products;
	private String remark;
}
