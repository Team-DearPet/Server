package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.PrePayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * PrePayment Repository
 * @Author 위지훈
 * @Since 2024.10.30
 */
public interface PrePaymentRepository extends JpaRepository<PrePayment, Long> {
    Optional<PrePayment> findByMerchantUid(String merchantUid);
}
