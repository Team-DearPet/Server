package com.dearpet.dearpet.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/*
 * Verification DTD
 * @Author 위지훈
 * @Since 2024.10.30
 */
@Getter
@Setter
public class VerificationDTO {
    private String merchantUid;         // 주문 고유 ID
    private BigDecimal expectedAmount;  // 예상 결제 금액
}
