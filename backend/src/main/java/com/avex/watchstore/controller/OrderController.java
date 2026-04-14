package com.avex.watchstore.controller;

import com.avex.watchstore.model.Order;
import com.avex.watchstore.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request.getUserId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrdersForUser(userId));
    }

    @GetMapping("/{userId}/{orderId}")
    public ResponseEntity<Order> getOrderForUser(
            @PathVariable String userId,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderForUser(userId, orderId));
    }

    @Data
    public static class CreateOrderRequest {
        @NotBlank(message = "User ID is required")
        private String userId;
    }
}
