package com.portal.kamsid.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portal.kamsid.entity.DailyProductionMaster;

public interface DailyProductionRepository extends JpaRepository<DailyProductionMaster, Long> {
	List<DailyProductionMaster> findByDateBetween(LocalDate start, LocalDate end);

	@Query("select distinct d from DailyProductionMaster d " + "left join fetch d.products pd "
			+ "left join fetch pd.product p")
	List<DailyProductionMaster> findAllWithProducts();

	@Query("select distinct d from DailyProductionMaster d " + "left join fetch d.products pd "
			+ "left join fetch pd.product p " + "where d.date between :start and :end")
	List<DailyProductionMaster> findByDateBetweenWithProducts(LocalDate start, LocalDate end);
}
