package com.portal.kamsid.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "product_details")
public class ProductDetails {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pdId")
    private Long pdId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_production_master_id", nullable = false)
    private DailyProductionMaster dailyProductionMaster;

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
}
