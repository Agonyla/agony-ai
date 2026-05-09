package com.agony.springai.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * @author: Agony
 * @create: 2026/5/9 15:04
 * @describe:
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer stock;

    private Double rating;

    private String description;

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public Double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }
}