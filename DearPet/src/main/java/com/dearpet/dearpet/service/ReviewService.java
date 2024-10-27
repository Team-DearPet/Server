package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.ReviewDTO;
import com.dearpet.dearpet.entity.Review;
import com.dearpet.dearpet.repository.ProductRepository;
import com.dearpet.dearpet.repository.ReviewRepository;
import com.dearpet.dearpet.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Review Service
 * @Author ghpark
 * @Since 2024.10.28
 */
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // 특정 상품에 대한 모든 리뷰 조회
    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductProductId(productId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 특정 리뷰 상세 조회
    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return convertToDto(review);
    }

    // 리뷰 작성
    @Transactional
    public ReviewDTO createReview(Long userId, Long productId, ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
        review.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found")));
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setImage(reviewDTO.getImage());
        review.setReviewedAt(new Timestamp(System.currentTimeMillis()));

        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }

    // 리뷰 수정
    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setImage(reviewDTO.getImage());
        review.setReviewedAt(new Timestamp(System.currentTimeMillis()));

        Review updatedReview = reviewRepository.save(review);
        return convertToDto(updatedReview);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // 엔티티를 DTO로 변환
    private ReviewDTO convertToDto(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setUserId(review.getUser().getUserId());
        dto.setProductId(review.getProduct().getProductId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setImage(review.getImage());
        dto.setReviewedAt(review.getReviewedAt());
        return dto;
    }
}
