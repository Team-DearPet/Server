package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.CategoryDTO;
import com.dearpet.dearpet.entity.Category;
import com.dearpet.dearpet.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Category Service
 * @Author 위지훈
 * @Since 2024.10.25
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // 카테고리 목록 조회
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::convertToCategoryDTO).collect(Collectors.toList());
    }

    // 카테고리 등록
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());

        categoryRepository.save(category);
        return convertToCategoryDTO(category);
    }

    // 카테고리 삭제
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    // 카테고리 Entity -> DTO 변환
    private CategoryDTO convertToCategoryDTO(Category category) {
        return new CategoryDTO(category.getCategoryId(), category.getName());
    }
}
