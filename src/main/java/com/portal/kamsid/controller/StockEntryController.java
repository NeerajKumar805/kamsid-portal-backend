package com.portal.kamsid.controller;

import com.portal.kamsid.dto.StockEntryDto;
import com.portal.kamsid.service.StockEntryService;
import com.portal.kamsid.util.ApiPaths;
import com.portal.kamsid.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(ApiPaths.INVENTORY_HISTORY )
@RequiredArgsConstructor
public class StockEntryController {

    private final StockEntryService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockEntryDto>>> listAllEntries(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String direction) {
        return ResponseEntity.ok(ApiResponse.success("stock entries fetched",
                service.find(productId, start, end, module, direction)));
    }
}
