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
    public CartDTO getCurrentUserCart(@RequestParam("userId") Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/items")
    public CartDTO addItemToCart(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId, @RequestParam("quantity") int quantity) {
        return cartService.addItemToCart(userId, productId, quantity);
    }

    @PatchMapping("/items/{cartItemId}")
    public CartDTO updateCartItem(@PathVariable("cartItemId") Long cartItemId, @RequestParam("quantity") int quantity) {
        return cartService.updateCartItem(cartItemId, quantity);
    }

    @DeleteMapping("/items/{cartItemId}")
    public void deleteCartItem(@PathVariable("cartItemId") Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
    }

    @PostMapping("/checkout")
    public void checkout(@RequestParam("userId") Long userId) {
        cartService.checkout(userId);
    }
}