package com.portal.kamsid.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class DailyStockMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "bill_no")
	private String billNo;

	@Builder.Default
	@OneToMany(mappedBy = "stockMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductDetails> products = new ArrayList<>();

	public void addProductDetails(ProductDetails pd) {
		pd.setStockMaster(this);
		this.products.add(pd);
	}

	public void removeProductDetails(ProductDetails pd) {
		this.products.remove(pd);
		pd.setStockMaster(null);
	}
}
