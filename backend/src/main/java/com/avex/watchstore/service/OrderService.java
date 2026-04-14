package com.avex.watchstore.service;

import com.avex.watchstore.model.*;
import com.avex.watchstore.repository.CartItemRepository;
import com.avex.watchstore.repository.OrderRepository;
import com.avex.watchstore.repository.WatchRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final WatchRepository watchRepository;

    public List<Order> getOrdersForUser(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Order getOrderForUser(String userId, Long orderId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    @Transactional
    public Order createOrder(String userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot create order: cart is empty for user " + userId);
        }

        Order order = Order.builder()
                .userId(userId)
                .status(Order.OrderStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        // Batch-fetch all watches to avoid N+1 queries
        List<Long> watchIds = cartItems.stream().map(CartItem::getWatchId).collect(Collectors.toList());
        Map<Long, Watch> watchMap = watchRepository.findAllById(watchIds).stream()
                .collect(Collectors.toMap(Watch::getId, Function.identity()));

        for (CartItem cartItem : cartItems) {
            Watch watch = watchMap.get(cartItem.getWatchId());
            if (watch == null) {
                throw new EntityNotFoundException("Watch not found with id: " + cartItem.getWatchId());
            }

            if (watch.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for watch: " + watch.getName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .watchId(watch.getId())
                    .watchName(watch.getName())
                    .price(watch.getPrice())
                    .quantity(cartItem.getQuantity())
                    .order(order)
                    .build();

            order.getItems().add(orderItem);
            total = total.add(watch.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            // Deduct stock
            watch.setStockQuantity(watch.getStockQuantity() - cartItem.getQuantity());
            watchRepository.save(watch);
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after successful order
        cartItemRepository.deleteByUserId(userId);

        return savedOrder;
    }
}
