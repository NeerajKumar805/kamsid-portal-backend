package com.portal.kamsid.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // exact field names per earlier discussion (use product_name for clarity)
    @Column(name = "product_name", nullable = false)
    private String productName;

    private String type;
    private String colour;
    private String unit;
    private String weight;
    private String quantity;
}
