package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/*
 * Review Repository
 * @Author ghpark
 * @Since 2024.10.28
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductProductId(Long productId); // 특정 상품에 대한 리뷰 목록 조회
}
