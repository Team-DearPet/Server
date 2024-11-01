package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * PrePayment
 * @Author ghpark
 * @Since 2024.11.01
 */
@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "buyer_addr")
    private String buyerAddr;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_tel")
    private String buyerTel;

    @Column(name = "cancel_amount", columnDefinition = "DECIMAL(10, 2) DEFAULT 0")
    private BigDecimal cancelAmount = BigDecimal.ZERO;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "cancel_history")
    private String cancelHistory;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cash_receipt_issued", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean cashReceiptIssued = false;

    @Column(nullable = false)
    private String currency = "KRW";

    @ManyToOne
    @JoinColumn(name = "customer_uid", referencedColumnName = "user_id")
    private User customer;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean escrow = false;

    @Column(name = "imp_uid", unique = true, nullable = false)
    private String impUid;

    @Column(name = "merchant_uid", unique = true, nullable = false)
    private String merchantUid;

    private String name;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @Column(name = "pay_method")
    private String payMethod;

    @Column(name = "pg_provider")
    private String pgProvider;

    @Column(name = "pg_tid")
    private String pgTid;

    private String status;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", unique = true)
    private Order order;
}
