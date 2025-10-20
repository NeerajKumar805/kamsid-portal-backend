package com.portal.kamsid.service;

import java.util.List;

import com.portal.kamsid.dto.ProductResponseDto;

public interface ProductService {

	ProductResponseDto create(ProductResponseDto dto);

	List<ProductResponseDto> getAll();

	ProductResponseDto getById(Long id);
}
