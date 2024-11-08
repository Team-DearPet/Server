package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Cart Repository
 * @Author ghpark
 * @Since 2024.10.28
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserUserId(Long userId);
    Optional<Cart> findByUserUserIdAndStatus(Long userId, Cart.CartStatus status);
}