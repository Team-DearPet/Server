package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Product Repository
 * @Author 위지훈
 * @Since 2024.10.25
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    // categoryId로 카테고리 종류 조회
    List<Product> findByCategoryCategoryId(Long categoryId);
}
