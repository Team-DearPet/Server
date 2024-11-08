package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Verification Repository
 * @Author 위지훈
 * @Since 2024.10.30
 */
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByMerchantUid(String merchantUid);
}
