package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.OrderDTO;
import com.dearpet.dearpet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * OrderController
 * @Author 위지훈
 * @Since 2024.10.24
 */
@RestController
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
/* 토큰에서 userId를 뽑아와야함
    // 사용자 주문 내역 조회
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrderByUserId(){

        List<OrderDTO> oderList = orderService.getOrderByUserId(userId);
        return ResponseEntity.ok(orderList);
    }
*/
    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO){
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    // 주문 정보 상세 조회

    // 주문 취소
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId){
        OrderDTO cancelledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(cancelledOrder);

    }

    // 주문한 상품 목록 조회
}
