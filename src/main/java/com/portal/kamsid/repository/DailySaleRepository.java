package com.portal.kamsid.repository;

import com.portal.kamsid.entity.DailySaleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailySaleRepository extends JpaRepository<DailySaleMaster, Long> {
    List<DailySaleMaster> findByDateBetween(LocalDate start, LocalDate end);
}
