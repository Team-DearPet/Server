package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // userId로 사용자 주문 내역 조회
    List<Order> findByUserUserId(Long userId);
}
