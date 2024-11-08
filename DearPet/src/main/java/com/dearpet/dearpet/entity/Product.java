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
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;         // PK
    private String name;            // 상품명
    private String description;     // 상품 설명
    private String image;           // 상품 이미지
    private Integer quantity;           // 상품 수량

    @Column(precision = 10, scale = 2)
    private BigDecimal price;       // 상품 가격

    @Enumerated(EnumType.STRING)
    private ProductStatus status;   // 상품 상태

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;      // 상품 : 카테고리 = N : 1

    @Column(precision = 10, scale = 2, nullable = false, columnDefinition = "DECIMAL(10, 2) DEFAULT 0.00")
    private BigDecimal discount = BigDecimal.ZERO; // 할인율 (기본값 0)

    @Column(length = 50)
    private String seller;          // 상품 등록 업체명 (null 가능)

    public enum ProductStatus {
        AVAILABLE, SOLD_OUT, DISCONTINUED
    }
}
