package com.portal.kamsid.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portal.kamsid.entity.DailySaleMaster;

public interface DailySaleRepository extends JpaRepository<DailySaleMaster, Long> {
    
    @Query("select distinct d from DailySaleMaster d " + "left join fetch d.products pd "
			+ "left join fetch pd.product p")
	List<DailySaleMaster> findAllWithProducts();

	@Query("select distinct d from DailySaleMaster d " + "left join fetch d.products pd "
			+ "left join fetch pd.product p " + "where pd.date between :start and :end")
	List<DailySaleMaster> findByDateBetweenWithProducts(LocalDate start, LocalDate end);
}
