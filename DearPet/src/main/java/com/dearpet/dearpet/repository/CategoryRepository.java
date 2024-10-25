package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Category Repository
 * @Author 위지훈
 * @Since 2024.10.25
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
