package com.portal.kamsid.service;

import com.portal.kamsid.dto.ProductRequestDto;
import com.portal.kamsid.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto dto);
    List<ProductResponseDto> getAll();
    ProductResponseDto getById(Long id);
}
