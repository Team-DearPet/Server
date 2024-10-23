package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "Pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;                 // PK
    private String name;             // 펫 이름
    private String species;             // 펫 종류
    private int age;                    // 펫 나이
    private boolean neutered = false;   // 펫 중성화 여부

    @Enumerated(EnumType.STRING)
    private Gender gender;              // 펫 성별

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;          // 펫 몸무게

    @Lob
    private String healthStatus;        // 펫 건강상태

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum Gender{
        MALE, FEMALE
    }
}
