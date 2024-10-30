package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.*;

/*
 * Address Entity
 * @Author ghpark
 * @Since 2024.10.28
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원 ID와 연관 관계 설정

    private String address; // 실제 배송지 주소
    private Boolean defaultAddress; // 기본 배송지 여부
}