package com.portal.kamsid.controller;

import com.portal.kamsid.dto.DailyProductionRequestDto;
import com.portal.kamsid.dto.DailyProductionResponseDto;
import com.portal.kamsid.service.DailyProductionService;
import com.portal.kamsid.util.ApiPaths;
import com.portal.kamsid.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(ApiPaths.DAILY_PRODUCTION)
public class DailyProductionController {

    private final DailyProductionService service;
    public DailyProductionController(DailyProductionService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ApiResponse<DailyProductionResponseDto>> create(@Valid @RequestBody DailyProductionRequestDto dto) {
        DailyProductionResponseDto created = service.create(dto);
        return ResponseEntity.status(201).body(ApiResponse.success("Daily production created", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DailyProductionResponseDto>>> all(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        List<DailyProductionResponseDto> data;
        if (start != null && end != null) {
            data = service.getByDateRange(start, end);
        } else {
            data = service.getAll();
        }
        return ResponseEntity.ok(ApiResponse.success("Daily production fetched", data));
    }
}
