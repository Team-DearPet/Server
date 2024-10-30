package com.dearpet.dearpet.dto;

import java.sql.Timestamp;
import lombok.*;

/*
 * Review DTO
 * @Author ghpark
 * @Since 2024.10.28
 */
@Getter
@Setter
public class ReviewDTO {
    private Long reviewId;
    private Long userId;
    private Long productId;
    private int rating;
    private String comment;
    private String image;
    private Timestamp reviewedAt;
    private String nickname;
}