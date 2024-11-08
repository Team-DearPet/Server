package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/*
 * OrderItem Entity
 * @Author 위지훈
 * @Since 2024.10.24
 */
@Entity
@Getter
@Setter
@Table(name = "orderitems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;       // PK
    private int quantity;           // 수량

    @Column(precision = 10, scale = 2)
    private BigDecimal price;       // 가격

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;            // 주문 상세 : 주문 = N : 1

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;        // 주문 상세 : 상품 = N : 1
}
