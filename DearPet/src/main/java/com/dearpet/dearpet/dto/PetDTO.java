package com.dearpet.dearpet.dto;

import java.math.BigDecimal;

public class PetDTO {
    private Long petId;                 // PK
    private String name;                // 펫 이름
    private String species;             // 펫 종류
    private int age;                    // 펫 나이
    private boolean neutered;   // 펫 중성화 여부
    private String gender;              // 펫 성별
    private BigDecimal weight;          // 펫 몸무게
    private String healthStatus;        // 펫 건강상태

    // 기본 생성자
    public PetDTO() {}

    // 생성자
    public PetDTO(Long petId, String name, String species, int age, boolean neutered, String gender, BigDecimal weight, String healthStatus) {
        this.petId = petId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.neutered = neutered;
        this.gender = gender;
        this.weight = weight;
        this.healthStatus = healthStatus;
    }

    // Getter/Setter
    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }
}
