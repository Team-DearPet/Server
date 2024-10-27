package com.dearpet.dearpet.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/*
 * Cart DTO
 * @Author ghpark
 * @Since 2024.10.28
 */
@Getter
@Setter
public class CartDTO {
    private Long cartId;
    private List<CartItemDTO> items;
    private BigDecimal totalPrice;

    // Getters and Setters
}
