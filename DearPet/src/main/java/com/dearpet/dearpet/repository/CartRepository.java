package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Cart Repository
 * @Author ghpark
 * @Since 2024.10.28
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserUserId(Long userId);
}