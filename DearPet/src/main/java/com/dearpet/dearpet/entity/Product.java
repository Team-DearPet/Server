package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/*
 * Product Entity
 * @Author 위지훈
 * @Since 2024.10.25
 */
@Entity
@Getter
@Setter
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;         // PK
    private String name;            // 상품명
    private String description;     // 상품 설명
    private String image;           // 상품 이미지
    private int quantity;           // 상품 수량

    @Column(precision = 10, scale = 2)
    private BigDecimal price;       // 상품 가격

    @Enumerated(EnumType.STRING)
    private ProductStatus status;   // 상품 상태

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;      // 상품 : 카테고리 = N : 1

    public enum ProductStatus {
        AVAILABLE, SOLD_OUT, DISCONTINUED
    }
}
