package com.portal.kamsid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portal.kamsid.entity.StockEntry;

public interface StockEntryRepository extends JpaRepository<StockEntry, Long> {
}
