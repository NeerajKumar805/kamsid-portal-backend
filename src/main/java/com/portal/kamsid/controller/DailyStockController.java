package com.portal.kamsid.controller;

import com.portal.kamsid.dto.DailyStockRequestDto;
import com.portal.kamsid.dto.DailyStockResponseDto;
import com.portal.kamsid.service.DailyStockService;
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
@RequestMapping(ApiPaths.DAILY_STOCK)
public class DailyStockController {

    private final DailyStockService service;

    public DailyStockController(DailyStockService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<DailyStockResponseDto>>>create(
            @Valid @RequestBody DailyStockRequestDto dto) {
        List<DailyStockResponseDto> created = service.create(dto);
        return ResponseEntity.status(201).body(ApiResponse.success("Daily stock created", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DailyStockResponseDto>>> all(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        List<DailyStockResponseDto> data =
                (start != null && end != null) ? service.getByDateRange(start, end) : service.getAll();
        return ResponseEntity.ok(ApiResponse.success("Daily stock fetched", data));
    }
}
