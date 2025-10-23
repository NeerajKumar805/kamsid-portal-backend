package com.portal.kamsid.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.kamsid.dto.StockEntryDto;
import com.portal.kamsid.entity.StockEntry;
import com.portal.kamsid.service.StockEntryService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockEntryServiceImpl implements StockEntryService {

	private final EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public List<StockEntryDto> find(Long productId, LocalDate start, LocalDate end, String module, String direction) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<StockEntry> cq = cb.createQuery(StockEntry.class);
		Root<StockEntry> root = cq.from(StockEntry.class);
		Predicate predicate = cb.conjunction();

		if (productId != null) {
			predicate = cb.and(predicate, cb.equal(root.get("product").get("pid"), productId));
		}
		if (start != null) {
			predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("entryDate"), start));
		}
		if (end != null) {
			predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("entryDate"), end));
		}
		if (module != null && !module.isBlank()) {
			predicate = cb.and(predicate, cb.equal(root.get("sourceModule"), module));
		}
		if (direction != null && !direction.isBlank()) {
			predicate = cb.and(predicate, cb.equal(root.get("direction"), direction));
		}

		cq.select(root).where(predicate).orderBy(cb.desc(root.get("entryDate")), cb.desc(root.get("id")));
		TypedQuery<StockEntry> q = em.createQuery(cq);
		List<StockEntry> results = q.getResultList();

		return results.stream().map(this::toDto).collect(Collectors.toList());
	}

	private StockEntryDto toDto(StockEntry se) {
		StockEntryDto dto = new StockEntryDto();
		dto.setId(se.getId());
		dto.setProductId(se.getProduct().getPid());
		dto.setProductName(se.getProduct().getProductName());
		dto.setQuantity(se.getQuantity());
		dto.setDirection(se.getDirection().name());
		dto.setSourceModule(se.getSourceModule());
		dto.setReferenceMasterId(se.getReferenceMasterId());
		dto.setReferenceProductDetailsId(se.getReferenceProductDetailsId());
		dto.setEntryDate(se.getEntryDate());
		dto.setRemark(se.getRemark());
		return dto;
	}
}
