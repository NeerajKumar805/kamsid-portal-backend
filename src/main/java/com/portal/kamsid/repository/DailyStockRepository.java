package com.portal.kamsid.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portal.kamsid.entity.DailyStockMaster;

public interface DailyStockRepository extends JpaRepository<DailyStockMaster, Long> {

	@Query("select distinct d from DailyStockMaster d " + "left join fetch d.products pd "
			+ "left join fetch pd.product p")
	List<DailyStockMaster> findAllWithProducts();

	@Query("select distinct d from DailyStockMaster d " + "left join fetch d.products pd "
			+ "left join fetch pd.product p " + "where pd.date between :start and :end")
	List<DailyStockMaster> findByDateBetweenWithProducts(LocalDate start, LocalDate end);

}
