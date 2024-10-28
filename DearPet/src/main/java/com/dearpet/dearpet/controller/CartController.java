package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.CartDTO;
import com.dearpet.dearpet.service.CartService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
 * Cart Controller
 * @Author ghpark
 * @Since 2024.10.28
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartDTO getCurrentUserCart(@RequestParam Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/items")
    public CartDTO addItemToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        return cartService.addItemToCart(userId, productId, quantity);
    }

    @PatchMapping("/items/{cart_item_id}")
    public CartDTO updateCartItem(@PathVariable("cart_item_id") Long cartItemId, @RequestParam int quantity) {
        return cartService.updateCartItem(cartItemId, quantity);
    }

    @DeleteMapping("/items/{cart_item_id}")
    public void deleteCartItem(@PathVariable("cart_item_id") Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
    }

    @PostMapping("/checkout")
    public void checkout(@RequestParam Long userId) {
        cartService.checkout(userId);
    }
}