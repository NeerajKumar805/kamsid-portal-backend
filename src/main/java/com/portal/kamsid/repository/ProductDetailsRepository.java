package com.portal.kamsid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portal.kamsid.entity.ProductDetails;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {
}
