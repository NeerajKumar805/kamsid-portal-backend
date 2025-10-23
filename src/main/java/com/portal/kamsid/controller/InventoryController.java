package com.portal.kamsid.controller;

import com.portal.kamsid.dto.InventoryDto;
import com.portal.kamsid.dto.StockEntryDto;
import com.portal.kamsid.service.InventoryService;
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
@RequestMapping(ApiPaths.INVENTORY)
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // current on-hand list
    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryDto>>> listAll() {
        return ResponseEntity.ok(ApiResponse.success("Inventory fetched", inventoryService.listAll()));
    }

    // single product inventory
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<InventoryDto>> get(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success("Inventory fetched", inventoryService.getByProductId(productId)));
    }

    // history for a single product
    @GetMapping("/{productId}/entries")
    public ResponseEntity<ApiResponse<List<StockEntryDto>>> entriesForProduct(
            @PathVariable Long productId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String direction) {

        return ResponseEntity.ok(ApiResponse.success("Stock entries fetched",
                inventoryService.findEntries(productId, start, end, module, direction)));
    }

    // global history (productId optional)
    @GetMapping("/entries")
    public ResponseEntity<ApiResponse<List<StockEntryDto>>> entries(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String direction) {

        return ResponseEntity.ok(ApiResponse.success("Stock entries fetched",
                inventoryService.findEntries(productId, start, end, module, direction)));
    }

    // admin-only: rebuild inventory from history (protect this)
    @PostMapping("/admin/rebuild")
    public ResponseEntity<ApiResponse<String>> rebuild() {
        inventoryService.rebuildInventoryFromEntries();
        return ResponseEntity.ok(ApiResponse.success("Inventory rebuild started", "ok"));
    }
}
