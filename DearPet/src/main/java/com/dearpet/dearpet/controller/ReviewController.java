package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.ReviewDTO;
import com.dearpet.dearpet.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
 * Review Controller
 * @Author ghpark
 * @Since 2024.10.28
 */
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin("*")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 상품 리뷰 목록 조회
    @GetMapping
    public List<ReviewDTO> getAllReviewsByProductId(@RequestParam("product_id") Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    // 리뷰 상세 정보 조회
    @GetMapping("/{review_id}")
    public ReviewDTO getReview(@PathVariable("review_id") Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    // 상품 리뷰 작성
    @PostMapping("/products/{product_id}/reviews")
    public ReviewDTO createReview(@RequestParam Long userId, @PathVariable("product_id") Long productId, @RequestBody ReviewDTO reviewDTO) {
        return reviewService.createReview(userId, productId, reviewDTO);
    }

    // 리뷰 수정
    @PatchMapping("/{review_id}")
    public ReviewDTO updateReview(@PathVariable("review_id") Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        return reviewService.updateReview(reviewId, reviewDTO);
    }

    // 리뷰 삭제
    @DeleteMapping("/{review_id}")
    public void deleteReview(@PathVariable("review_id") Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}