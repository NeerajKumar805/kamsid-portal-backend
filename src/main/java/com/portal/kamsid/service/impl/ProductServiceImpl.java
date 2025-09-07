package com.portal.kamsid.service.impl;

import com.portal.kamsid.dto.ProductRequestDto;
import com.portal.kamsid.dto.ProductResponseDto;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    @Override
    public ProductResponseDto create(ProductRequestDto p) {
        Product build = Product.builder()
                .productName(p.getProductName())
                .colour(p.getColour())
                .type(p.getType())
                .unit(p.getUnit())
                .weight(p.getWeight())
                .quantity(p.getQuantity())
                .build();

        return toDto(repo.save(build));
    }

    @Override
    public List<ProductResponseDto> getAll() {
        return repo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        return toDto(p);
    }

    private ProductResponseDto toDto(Product p) {

        return ProductResponseDto.builder()
                .id(p.getId())
                .productName(p.getProductName())
                .colour(p.getColour())
                .type(p.getType())
                .unit(p.getUnit())
                .weight(p.getWeight())
                .quantity(p.getQuantity())
                .build();
    }
}
