package com.portal.kamsid.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal.kamsid.dto.ProductResponseDto;
import com.portal.kamsid.service.ProductService;
import com.portal.kamsid.util.ApiPaths;
import com.portal.kamsid.util.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping(ApiPaths.PRODUCTS)
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;

	@PostMapping
	public ResponseEntity<ApiResponse<ProductResponseDto>> create(@Valid @RequestBody ProductResponseDto dto) {
		ProductResponseDto created = service.create(dto);
		return ResponseEntity.status(201).body(ApiResponse.success("Product created", created));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<ProductResponseDto>>> all() {
		List<ProductResponseDto> list = service.getAll();
		return ResponseEntity.ok(ApiResponse.success("Products fetched", list));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ProductResponseDto>> getById(@PathVariable Long id) {
		ProductResponseDto dto = service.getById(id);
		return ResponseEntity.ok(ApiResponse.success("Product fetched", dto));
	}
}
