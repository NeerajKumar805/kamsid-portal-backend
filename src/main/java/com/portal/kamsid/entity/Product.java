package com.portal.kamsid.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // exact field names per earlier discussion (use product_name for clarity)
    @Column(name = "product_name", nullable = false)
    private String productName;

    private String colour;
    private String unit;
    private String weight;
    private String quantity;
}
