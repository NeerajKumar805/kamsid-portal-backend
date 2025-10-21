package com.portal.kamsid.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portal.kamsid.dto.DailyMasterRequestDto;
import com.portal.kamsid.dto.DailyMasterResponseDto;
import com.portal.kamsid.service.DailyStockService;
import com.portal.kamsid.util.ApiPaths;
import com.portal.kamsid.util.ApiResponse;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(ApiPaths.DAILY_STOCK)
public class DailyStockController {

	private final DailyStockService service;

	public DailyStockController(DailyStockService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<List<DailyMasterResponseDto>>> create(
			@Valid @RequestBody DailyMasterRequestDto dto) {
		List<DailyMasterResponseDto> created = service.create(dto);
		return ResponseEntity.status(201).body(ApiResponse.success("Daily stock created", created));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<DailyMasterResponseDto>>> all(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
		List<DailyMasterResponseDto> data = (start != null && end != null) ? service.getByDateRange(start, end)
				: service.getAll();
		return ResponseEntity.ok(ApiResponse.success("Daily stock fetched", data));
	}
}
