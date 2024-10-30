package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * Order Entity
 * @Author 위지훈
 * @Since 2024.10.24
 */
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;           // PK
    private LocalDateTime date;     // 주문일
    private String address;         // 수령 주소
    private String requirement;     // 배송 요청사항
    private LocalDateTime eta;      // 배송 예정일

    @Enumerated(EnumType.STRING)
    private OrderStatus status;     // 배송 상태

    @Column(precision = 10, scale = 2)
    private BigDecimal price;       // 총 금액

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;              // 주문 : 사용자 = N : 1

    public enum OrderStatus {
        PENDING, SHIPPED, DELIVERED, CANCELLED
    }
}
