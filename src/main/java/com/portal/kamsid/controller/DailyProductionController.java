package com.portal.kamsid.controller;

import com.portal.kamsid.dto.DailyMasterRequestDto;
import com.portal.kamsid.dto.DailyMasterResponseDto;
import com.portal.kamsid.service.DailyProductionService;
import com.portal.kamsid.util.ApiPaths;
import com.portal.kamsid.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(ApiPaths.DAILY_PRODUCTION)
@RequiredArgsConstructor
public class DailyProductionController {

	private final DailyProductionService service;

	@PostMapping
	public ResponseEntity<ApiResponse<List<DailyMasterResponseDto>>> create(
			@Valid @RequestBody DailyMasterRequestDto dto) {
		List<DailyMasterResponseDto> created = service.create(dto);
		return ResponseEntity.status(201).body(ApiResponse.success("Daily production created", created));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<DailyMasterResponseDto>>> all(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
		List<DailyMasterResponseDto> data;
		if (start != null && end != null) {
			data = service.getByDateRange(start, end);
		} else {
			data = service.getAll();
		}
		return ResponseEntity.ok(ApiResponse.success("Daily production fetched", data));
	}
}
