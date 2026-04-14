package com.avex.watchstore.controller;

import com.avex.watchstore.model.CartItem;
import com.avex.watchstore.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartItem> addItem(
            @PathVariable String userId,
            @Valid @RequestBody AddItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addItem(userId, request.getWatchId(), request.getQuantity()));
    }

    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartItem> updateItemQuantity(
            @PathVariable String userId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, itemId, request.getQuantity()));
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable String userId, @PathVariable Long itemId) {
        cartService.removeItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class AddItemRequest {
        @NotNull(message = "Watch ID is required")
        private Long watchId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }

    @Data
    public static class UpdateQuantityRequest {
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }
}
