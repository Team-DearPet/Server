package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    // userId로 사용자가 보유한 펫 목록 조회
    List<Pet> findByUserUserId(Long userId);

    // petId로 펫 세부 정보 조회
    Optional<Pet> findByPetId(Long petId);
}
