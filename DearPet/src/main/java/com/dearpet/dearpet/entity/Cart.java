package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/*
 * Cart Entity
 * @Author ghpark
 * @Since 2024.10.28
 */
@Entity
@Getter
@Setter
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.OPEN;

    public enum CartStatus {
        OPEN, CHECKOUT
    }
}
