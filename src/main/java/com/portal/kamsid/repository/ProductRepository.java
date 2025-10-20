package com.portal.kamsid.repository;

import com.portal.kamsid.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsByProductNameIgnoreCase(String name);}
