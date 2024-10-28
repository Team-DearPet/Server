package com.dearpet.dearpet.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/*
 * CartItem DTO
 * @Author ghpark
 * @Since 2024.10.28
 */
@Getter
@Setter
public class CartItemDTO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;

    // Getters and Setters
}
