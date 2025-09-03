package com.portal.kamsid.repository;

import com.portal.kamsid.entity.DailyProductionMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyProductionRepository extends JpaRepository<DailyProductionMaster, Long> {
    List<DailyProductionMaster> findByDateBetween(LocalDate start, LocalDate end);
}
