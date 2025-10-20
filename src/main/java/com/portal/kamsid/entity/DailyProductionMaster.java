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
@Table(name = "daily_production_master")
public class DailyProductionMaster {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "remark")
    private String remark;

    @Builder.Default
    @OneToMany(mappedBy = "dailyProductionMaster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetails> products = new ArrayList<>();

    public void addProductDetails(ProductDetails pd) {
        pd.setDailyProductionMaster(this);
        this.products.add(pd);
    }

    public void removeProductDetails(ProductDetails pd) {
        this.products.remove(pd);
        pd.setDailyProductionMaster(null);
    }
}
