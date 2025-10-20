package com.portal.kamsid.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.kamsid.dto.ProductResponseDto;
import com.portal.kamsid.entity.Product;
import com.portal.kamsid.repository.ProductRepository;
import com.portal.kamsid.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repo;

	@Override
	public ProductResponseDto create(ProductResponseDto dto) {
		String name = dto.getProductName().trim();

		// ensure unique product name
		if (repo.existsByProductNameIgnoreCase(name)) {
			throw new IllegalArgumentException("Product with name '" + name + "' already exists");
		}

		Product saved = repo.save(Product.builder().productName(name).build());

		return ProductResponseDto.builder().id(saved.getPid()).productName(saved.getProductName()).build();
	}

	@Override
	public List<ProductResponseDto> getAll() {
		return repo.findAll().stream()
				.map(p -> ProductResponseDto.builder().id(p.getPid()).productName(p.getProductName()).build())
				.collect(Collectors.toList());
	}

	@Override
	public ProductResponseDto getById(Long id) {
		Product p = repo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

		return ProductResponseDto.builder().id(p.getPid()).productName(p.getProductName()).build();
	}
}
