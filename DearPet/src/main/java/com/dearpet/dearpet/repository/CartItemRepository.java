package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * CartItem Repository
 * @Author ghpark
 * @Since 2024.10.28
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}