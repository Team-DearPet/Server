package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.CartDTO;
import com.dearpet.dearpet.dto.OrderDTO;
import com.dearpet.dearpet.service.CartService;
import com.dearpet.dearpet.security.JwtTokenProvider;
import com.dearpet.dearpet.service.OrderService;
import org.springframework.http.ResponseEntity;
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
    private final JwtTokenProvider jwtTokenProvider;


    public CartController(CartService cartService, JwtTokenProvider jwtTokenProvider) {
        this.cartService = cartService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 현재 사용자의 장바구니 조회
    @GetMapping
    public ResponseEntity<CartDTO> getCurrentUserCart(@RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserId(token);
        CartDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    // 장바구니에 상품 추가
    @PostMapping("/items")
    public ResponseEntity<CartDTO> addItemToCart(@RequestHeader("Authorization") String token,
                                                 @RequestParam("productId") Long productId,
                                                 @RequestParam("quantity") int quantity) {
        Long userId = jwtTokenProvider.getUserId(token);
        CartDTO updatedCart = cartService.addItemToCart(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // 장바구니 내 특정 상품의 수량 수정
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                  @RequestParam("quantity") int quantity) {
        CartDTO updatedCart = cartService.updateCartItem(cartItemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // 장바구니에서 특정 상품 삭제
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable("cartItemId") Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

}