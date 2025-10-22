package com.portal.kamsid.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "product_details", indexes = {
		@Index(name = "idx_pd_production_master", columnList = "production_master_id"),
		@Index(name = "idx_pd_sale_master", columnList = "sale_master_id"),
		@Index(name = "idx_pd_stock_master", columnList = "stock_master_id") })
public class ProductDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pdId")
	private Long pdId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "production_master_id")
	private DailyProductionMaster productionMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_master_id")
	private DailySaleMaster saleMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_master_id")
	private DailyStockMaster stockMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "production_date", nullable = false)
	private LocalDate date;

	private String colour;
	private String type;
	private String unit;

	@Column(precision = 19, scale = 4)
	private BigDecimal weight;

	@Column(precision = 19, scale = 4)
	private BigDecimal quantity;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "bill_no")
	private String billNo;

	public void assignToProduction(DailyProductionMaster master) {
		clearParents();
		this.productionMaster = master;
		if (master != null && !master.getProducts().contains(this)) {
			master.getProducts().add(this);
		}
	}

	public void assignToSale(DailySaleMaster master) {
		clearParents();
		this.saleMaster = master;
		if (master != null && !master.getProducts().contains(this)) {
			master.getProducts().add(this);
		}
	}

	public void assignToStock(DailyStockMaster master) {
		clearParents();
		this.stockMaster = master;
		if (master != null && !master.getProducts().contains(this)) {
			master.getProducts().add(this);
		}
	}

	private void clearParents() {
		this.productionMaster = null;
		this.saleMaster = null;
		this.stockMaster = null;
	}

	public boolean hasExactlyOneParent() {
		int count = 0;
		if (productionMaster != null)
			count++;
		if (saleMaster != null)
			count++;
		if (stockMaster != null)
			count++;
		return count == 1;
	}
}
