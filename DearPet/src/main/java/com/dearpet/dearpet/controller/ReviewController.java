package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.ReviewDTO;
import com.dearpet.dearpet.security.JwtTokenProvider;
import com.dearpet.dearpet.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
 * Review Controller
 * @Author ghpark
 * @Since 2024.10.28
 */
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    public ReviewController(ReviewService reviewService, JwtTokenProvider jwtTokenProvider) {
        this.reviewService = reviewService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 상품 리뷰 목록 조회
    @GetMapping("/reviews")
    public List<ReviewDTO> getAllReviewsByProductId(@RequestParam("productId") Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    // 리뷰 상세 정보 조회
    @GetMapping("/reviews/{reviewId}")
    public ReviewDTO getReview(@PathVariable("reviewId") Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    // 상품 리뷰 작성
    @PostMapping("/products/{productId}/reviews")
    public ReviewDTO createReview(@RequestHeader("Authorization") String token, @PathVariable("productId") Long productId, @RequestBody ReviewDTO reviewDTO) {
        Long userId = jwtTokenProvider.getUserId(token); // JWT 토큰에서 userId 추출
        return reviewService.createReview(userId, productId, reviewDTO);
    }

    // 특정 상품에 대해 현재 로그인한 사용자가 이미 리뷰를 작성했는지 확인하는 엔드포인트 추가
    @GetMapping("/products/{productId}/has-reviewed")
    public boolean hasUserReviewedProduct(@RequestHeader("Authorization") String token, @PathVariable("productId") Long productId) {
        Long userId = jwtTokenProvider.getUserId(token); // JWT 토큰에서 userId 추출
        return reviewService.hasUserReviewedProduct(userId, productId);
    }

    // 리뷰 수정
    @PatchMapping("reviews/{reviewId}")
    public ReviewDTO updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        return reviewService.updateReview(reviewId, reviewDTO);
    }

    // 특정 사용자가 작성한 리뷰 목록 조회
    @GetMapping("reviews/user")
    public List<ReviewDTO> getReviewsByUser(@RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserId(token); // JWT 토큰에서 userId 추출
        return reviewService.getReviewsByUserId(userId);
    }

    // 리뷰 삭제
    @DeleteMapping("reviews/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
    }
}