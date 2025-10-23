package com.portal.kamsid.entity;

import java.math.BigDecimal;
import java.time.LocalDate;


import com.portal.kamsid.dto.StockDirection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(nullable = false, precision = 19, scale = 3)
	private BigDecimal quantity; // positive for IN, positive for OUT too; use direction enum

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private StockDirection direction; // IN or OUT

	@Column(name = "source_module", length = 50, nullable = false)
	private String sourceModule; // "PRODUCTION", "SALE", "STOCK"

	@Column(name = "reference_master_id")
	private Long referenceMasterId; // points to master id (production/sale/stock master)

	@Column(name = "reference_product_details_id")
	private Long referenceProductDetailsId; // optional pdId

	@Column(name = "entry_date", nullable = false)
	private LocalDate entryDate;

	@Column(length = 255)
	private String remark;
}
