package com.portal.kamsid.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.portal.kamsid.entity.Product;
import com.portal.kamsid.entity.ProductInventory;

import jakarta.persistence.LockModeType;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<ProductInventory> findByProduct(Product product);
	
	// read-only fetch by product id (no lock)
    Optional<ProductInventory> findByProduct_Pid(Long pid);
}
