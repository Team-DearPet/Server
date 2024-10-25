package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Order Repository
 * @Author 위지훈
 * @Since 2024.10.24
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // orderId로 주문한 상품 목록 조회
    List<OrderItem> findByOrderOrderId(Long orderId);
}
