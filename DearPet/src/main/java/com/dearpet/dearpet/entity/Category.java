package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
 * Category Entity
 * @Author 위지훈
 * @Since 2024.10.25
 */
@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;        // PK
    private String name;            // 카테고리명
}
