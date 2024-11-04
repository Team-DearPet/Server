package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.CartDTO;
import com.dearpet.dearpet.dto.CartItemDTO;
import com.dearpet.dearpet.entity.Cart;
import com.dearpet.dearpet.entity.CartItem;
import com.dearpet.dearpet.entity.Product;
import com.dearpet.dearpet.repository.CartItemRepository;
import com.dearpet.dearpet.repository.CartRepository;
import com.dearpet.dearpet.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Cart Service
 * @Author ghpark
 * @Since 2024.10.28
 */
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository; // 상품 정보를 불러오기 위한 Repository

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    // 특정 사용자 ID에 해당하는 OPEN 상태의 장바구니 조회
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserUserIdAndStatus(userId, Cart.CartStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("No open cart found for user"));
        return convertToDto(cart);
    }

    // 사용자 장바구니에 새로운 상품을 추가하는 메서드
    @Transactional
    public CartDTO addItemToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found")); // 상품 존재 여부 확인

        // 이미 장바구니에 있는 상품인지 확인
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // 이미 존재하는 상품이라면 수량만 추가
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        } else {
            // 새 상품 추가
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getCartItems().add(cartItem);
        }

        // 장바구니 총 금액 업데이트
        updateCartTotalPrice(cart);

        return convertToDto(cart);
    }

    // 장바구니 내 특정 상품의 수량을 수정하는 메서드
    @Transactional
    public CartDTO updateCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // 수량 업데이트 및 해당 상품 가격 재계산
        cartItem.setQuantity(quantity);
        cartItem.setPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity)));

        // 장바구니 총 금액 업데이트
        updateCartTotalPrice(cartItem.getCart());

        return convertToDto(cartItem.getCart());
    }

    // 장바구니 내 특정 상품을 삭제하는 메서드
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Cart cart = cartItem.getCart();
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        // 장바구니 총 금액 업데이트
        updateCartTotalPrice(cart);
    }

    // 장바구니 결제(체크아웃) 로직 처리
    // 결제 완료 시 장바구니 상태를 CHECKOUT으로 변경
    @Transactional
    public void checkout(Long userId) {
        Cart cart = cartRepository.findByUserUserIdAndStatus(userId, Cart.CartStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("No open cart found for user"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 결제 성공 후 장바구니 상태를 CHECKOUT으로 변경
        cart.setStatus(Cart.CartStatus.CHECKOUT);
        cart.setPrice(BigDecimal.ZERO); // 총 금액 초기화
        cart.getCartItems().clear(); // 장바구니 아이템 비우기

        cartRepository.save(cart);
    }

    // Cart 엔티티를 CartDTO로 변환하는 메서드
    private CartDTO convertToDto(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setTotalPrice(cart.getPrice());

        // CartItem을 CartItemDTO로 변환하여 CartDTO에 추가
        cartDTO.setItems(cart.getCartItems().stream().map(cartItem -> {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setCartItemId(cartItem.getCartItemId());
            cartItemDTO.setProductId(cartItem.getProduct().getProductId());
            cartItemDTO.setProductName(cartItem.getProduct().getName());
            cartItemDTO.setQuantity(cartItem.getQuantity());
            cartItemDTO.setPrice(cartItem.getPrice());
            return cartItemDTO;
        }).toList());

        return cartDTO;
    }

    // 장바구니의 총 금액을 재계산하는 메서드
    private void updateCartTotalPrice(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setPrice(totalPrice);
        cartRepository.save(cart);
    }

    // 결제된 상품을 CartItems에서 가져오는 메서드
    public List<CartItem> getCartItemsByIds(Long userId, List<Long> cartItemIds) {
        Cart cart = cartRepository.findByUserUserIdAndStatus(userId, Cart.CartStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Open cart not found for user"));
        return cart.getCartItems().stream()
                .filter(item -> cartItemIds.contains(item.getCartItemId()))
                .collect(Collectors.toList());
    }

    // 결제된 상품을 CartItems에서 삭제하는 메서드
    public void removeCartItems(Long userId, List<Long> cartItemIds) {
        List<CartItem> itemsToRemove = getCartItemsByIds(userId, cartItemIds);
        cartItemRepository.deleteAll(itemsToRemove);

        // 장바구니 총 금액 업데이트
        Cart cart = cartRepository.findByUserUserIdAndStatus(userId, Cart.CartStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Open cart not found for user"));
        updateCartTotalPrice(cart);

        cart.getCartItems().removeAll(itemsToRemove);
        updateCartTotalPrice(cart);
        cartRepository.save(cart);
    }

}