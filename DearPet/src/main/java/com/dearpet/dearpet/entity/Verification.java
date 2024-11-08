package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/*
 * Verification
 * @Author 위지훈
 * @Since 2024.10.30
 */
@Entity
@Getter
@Setter
@Table(name = "verification")
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    @Column(nullable = false, unique = true)
    private String merchantUid;                             // 주문 고유 ID

    @Column(precision = 10, scale = 2)
    private BigDecimal expectedAmount;                      // 예상 결제 금액

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;                 // 결제 상태

    private LocalDateTime createdAt = LocalDateTime.now();  // 생성 시각

    public enum Status {
        PENDING, CONFIRMED, FAILED
    }
}
