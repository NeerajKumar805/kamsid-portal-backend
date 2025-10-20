package com.portal.kamsid.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DailyProductionRequestDto {

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@NotEmpty
	@Valid
	private List<ProductRequestDto> products;
	private String remark;
}
