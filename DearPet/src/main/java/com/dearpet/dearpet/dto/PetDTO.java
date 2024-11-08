package com.dearpet.dearpet.dto;

import com.dearpet.dearpet.entity.Pet;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/*
 * Pet DTD
 * @Author 위지훈
 * @Since 2024.10.23
 */
@Getter
@Setter
public class PetDTO {
    private Long petId;                 // PK
    private String name;                // 펫 이름
    private String species;             // 펫 종류
    private Integer age;                    // 펫 나이
    private Boolean neutered;           // 펫 중성화 여부
    private Pet.Gender gender;          // 펫 성별
    private BigDecimal weight;          // 펫 몸무게
    private String healthStatus;        // 펫 건강상태
    private Long userId;                // 사용자 ID

    // 기본 생성자
    public PetDTO() {
    }

    // 생성자
    public PetDTO(Long petId, String name, String species, Integer age, Boolean neutered, Pet.Gender gender, BigDecimal weight, String healthStatus, Long userId) {
        this.petId = petId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.neutered = neutered;
        this.gender = gender;
        this.weight = weight;
        this.healthStatus = healthStatus;
        this.userId = userId;
    }

}
