package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.OrderDTO;
import com.dearpet.dearpet.dto.OrderItemDTO;
import com.dearpet.dearpet.entity.Order;
import com.dearpet.dearpet.entity.OrderItem;
import com.dearpet.dearpet.entity.User;
import com.dearpet.dearpet.repository.OrderItemRepository;
import com.dearpet.dearpet.repository.OrderRepository;
import com.dearpet.dearpet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
 * OrderService
 * @Author 위지훈
 * @Since 2024.10.24
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    // 사용자 주문 내역 조회
    public List<OrderDTO> getOrderByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> order = orderRepository.findByUserUserId(user.getUserId());
        return order.stream().map(this::convertToOrderDTO).collect(Collectors.toList());
    }

    // 주문 생성
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setDate(orderDTO.getDate());
        order.setStatus(orderDTO.getStatus());
        order.setPrice(orderDTO.getPrice());
        order.setAddress(orderDTO.getAddress());
        order.setRequirement(orderDTO.getRequirement());
        order.setEta(orderDTO.getEta());

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);

        orderRepository.save(order);
        return convertToOrderDTO(order);
    }

    // 주문 정보 상세 조회
    public OrderDTO getOrderByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToOrderDTO(order);
    }

    // 주문 취소 (주문 상태만 "CANCELLED"로 변경)
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("order not found"));

        order.setStatus(Order.OrderStatus.CANCELLED);

        Order cancelledOrder = orderRepository.save(order);
        return convertToOrderDTO(cancelledOrder);
    }

    // 주문한 상품 목록 조회
    public List<OrderItemDTO> getOrderItemByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderOrderId(orderId);
        return orderItems.stream().map(this::convertToOrderItemDTO).collect(Collectors.toList());
    }

    // 주문 Entity -> DTO 변환
    private OrderDTO convertToOrderDTO(Order order) {
        return new OrderDTO(order.getOrderId(), order.getDate(), order.getAddress(),
                order.getRequirement(), order.getEta(), order.getUser().getUserId(),
                order.getPrice(), order.getStatus());
    }

    // 주문 상세 Entity -> DTO 변환
    private OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(orderItem.getOrderItemId(), orderItem.getQuantity(),
                orderItem.getPrice(), orderItem.getOrder().getOrderId()
                , orderItem.getProduct().getProductId());
    }

}
