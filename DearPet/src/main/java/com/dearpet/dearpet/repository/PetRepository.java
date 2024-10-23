package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Pet Repository
 * @Author 위지훈
 * @Since 2024.10.23
 */
public interface PetRepository extends JpaRepository<Pet, Long> {

    // userId로 사용자가 보유한 펫 목록 조회
    List<Pet> findByUserUserId(Long userId);

}
