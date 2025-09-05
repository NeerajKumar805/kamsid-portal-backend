package com.portal.kamsid.repository;

import com.portal.kamsid.entity.DailyStockMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyStockRepository extends JpaRepository<DailyStockMaster, Long> {
    List<DailyStockMaster> findByDateBetween(LocalDate start, LocalDate end);
}
