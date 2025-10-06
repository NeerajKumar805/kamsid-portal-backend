package com.portal.kamsid.controller;

import com.portal.kamsid.dto.DailySaleRequestDto;
import com.portal.kamsid.dto.DailySaleResponseDto;
import com.portal.kamsid.service.DailySaleService;
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
@RequestMapping(ApiPaths.DAILY_SALE)
public class DailySaleController {

    private final DailySaleService service;

    public DailySaleController(DailySaleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<DailySaleResponseDto>>> create(
            @Valid @RequestBody DailySaleRequestDto dto) {
        List<DailySaleResponseDto> created = service.create(dto);
        return ResponseEntity.status(201).body(ApiResponse.success("Daily sale created", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DailySaleResponseDto>>> all(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        List<DailySaleResponseDto> data =
                (start != null && end != null) ? service.getByDateRange(start, end) : service.getAll();
        return ResponseEntity.ok(ApiResponse.success("Daily sale fetched", data));
    }
}