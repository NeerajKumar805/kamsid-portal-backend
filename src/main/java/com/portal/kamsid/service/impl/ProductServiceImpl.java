package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.ProductRequestDto;
import com.portal.kamsid.dto.ProductResponseDto;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) { this.repo = repo; }

    @Override
    public ProductResponseDto create(ProductRequestDto dto) {
        Product p = new Product();
        p.setProductName(dto.getProductName());
        p.setDescription(dto.getDescription());
        p.setCategory(dto.getCategory());
        p.setWeight(dto.getWeight());
        p.setSize(dto.getSize());
        Product saved = repo.save(p);
        return toDto(saved);
    }

    @Override
    public List<ProductResponseDto> getAll() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getById(Long id) {
        Product p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        return toDto(p);
    }

    private ProductResponseDto toDto(Product p) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(p.getId());
        dto.setProductName(p.getProductName());
        dto.setDescription(p.getDescription());
        dto.setCategory(p.getCategory());
        dto.setWeight(p.getWeight());
        dto.setSize(p.getSize());
        return dto;
    }
}
