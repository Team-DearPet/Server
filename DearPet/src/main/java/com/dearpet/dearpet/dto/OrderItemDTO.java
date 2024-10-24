package com.dearpet.dearpet.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/*
 * OrderItem DTO
 * @Author 위지훈
 * @Since 2024.10.24
 */
@Getter
@Setter
public class OrderItemDTO {
    private Long orderItemId;       // PK
    private int quantity;           // 수량
    private BigDecimal price;       // 가격
    private Long orderId;           // 주문 id
    private Long productId;         // 상품 id

    // 기본 생성자
    public OrderItemDTO() {}

    // 생성자
    public OrderItemDTO(Long orderItemId, int quantity, BigDecimal price, Long orderId, Long productId) {
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
        this.productId = productId;
    }
}
