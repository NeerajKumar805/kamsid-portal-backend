package com.portal.kamsid.controller;

import com.portal.kamsid.dto.ProductRequestDto;
import com.portal.kamsid.dto.ProductResponseDto;
import com.portal.kamsid.service.ProductService;
import com.portal.kamsid.util.ApiPaths;
import com.portal.kamsid.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(ApiPaths.PRODUCTS)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> create(@Valid @RequestBody ProductRequestDto dto) {
        ProductResponseDto created = service.create(dto);
        return ResponseEntity.status(201).body(ApiResponse.success("Product created", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> all() {
        return ResponseEntity.ok(ApiResponse.success("Products fetched", service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.getById(id)));
    }
}
