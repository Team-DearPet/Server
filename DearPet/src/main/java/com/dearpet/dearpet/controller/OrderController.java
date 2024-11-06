package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.OrderDTO;
import com.dearpet.dearpet.dto.OrderItemDTO;
import com.dearpet.dearpet.security.JwtTokenProvider;
import com.dearpet.dearpet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*
 * OrderController
 * @Author 위지훈
 * @Since 2024.10.24
 */
@RestController
@RequestMapping("api/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 사용자 주문 내역 조회
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrderByUsername(@RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsername(token);

        List<OrderDTO> orderList = orderService.getOrderByUsername(username);
        return ResponseEntity.ok(orderList);
    }

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    // 주문 정보 단일 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") Long orderId) {
        OrderDTO orderDTO = orderService.getOrderByOrderId(orderId);
        return ResponseEntity.ok(orderDTO);
    }

    // 주문 취소 엔드포인트
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable("orderId") Long orderId, @RequestBody Map<String, String> payload) {
        String reason = payload.get("reason"); // 환불 사유
        OrderDTO cancelledOrder = orderService.cancelOrder(orderId, reason);
        return ResponseEntity.ok(cancelledOrder);
    }

    // 주문한 상품 목록 조회
    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemDTO>> getOrderItems(@PathVariable("orderId") Long orderId) {
        List<OrderItemDTO> orderItemList = orderService.getOrderItemByOrderId(orderId);
        return ResponseEntity.ok(orderItemList);
    }

    // 결제시 주문 생성
    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout(@RequestHeader("Authorization") String token,
                                         @RequestParam("impUid") String impUid,
                                         @RequestParam(value = "cartItemIds", required = false) List<Long> cartItemIds,
                                         @RequestBody OrderDTO orderDTO) {
        Long userId = jwtTokenProvider.getUserId(token);
        orderService.createOrderFromPayment(userId, impUid, cartItemIds, orderDTO);
        return ResponseEntity.noContent().build();
    }

}
