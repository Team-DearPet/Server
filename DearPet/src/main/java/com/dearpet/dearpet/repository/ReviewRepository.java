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
    List<Review> findByUserUserId(Long userId); // 특정 사용자가 작성한 리뷰 목록 조회
    boolean existsByUserUserIdAndProductProductId(Long userId, Long productId); // 특정 사용자가 특정 상품에 대해 리뷰를 작성했는지 확인
}
