package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;            // PK
    private Date date;              // 주문일
    private String address;         // 수령 주소
    private String requirement;     // 배송 요청사항
    private Date eta;               // 배송 예정일

    @Enumerated(EnumType.STRING)
    private Status status;          // 배송 상태

    @Column(precision = 10, scale = 2)
    private BigDecimal price;       // 총 금액

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;              // 주문 : 사용자 = N : 1

    public enum Status {
        PENDING, SHIPPED, DELIVERED, CANCELLED
    }
}
