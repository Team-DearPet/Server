package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Order Repository
 * @Author 위지훈
 * @Since 2024.10.24
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    // userId로 사용자 주문 내역 조회
    List<Order> findByUserUserId(Long userId);
}
