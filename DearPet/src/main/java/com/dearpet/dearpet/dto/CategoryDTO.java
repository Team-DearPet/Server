package com.dearpet.dearpet.dto;

import lombok.Getter;
import lombok.Setter;

/*
 * Category DTO
 * @Author 위지훈
 * @Since 2024.10.25
 */
@Getter
@Setter
public class CategoryDTO {
    private Long categoryId;        // PK
    private String name;            // 카테고리명

    // 기본 생성자
    public CategoryDTO() {
    }

    // 생성자
    public CategoryDTO(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
