package com.dearpet.dearpet.dto;

import com.dearpet.dearpet.entity.Order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * Order DTO
 * @Author 위지훈
 * @Since 2024.10.24
 */
@Getter
@Setter
public class OrderDTO {
    private Long orderId;           // PK
    private LocalDateTime date;     // 주문일
    private String address;         // 수령 주소
    private String requirement;     // 배송 요청사항
    private LocalDateTime eta;      // 배송 예정일
    private Order.Status status;    // 배송 상태
    private BigDecimal price;       // 총 금액
    private Long userId;            // 사용자 ID

    // 기본 생성자
    public OrderDTO() {
    }

    // 생성자
    public OrderDTO(Long orderId, LocalDateTime date, String address, String requirement, LocalDateTime eta, Long userId, BigDecimal price, Order.Status status) {
        this.orderId = orderId;
        this.date = date;
        this.address = address;
        this.requirement = requirement;
        this.eta = eta;
        this.userId = userId;
        this.price = price;
        this.status = status;
    }

}
