package com.avex.watchstore.service;

import com.avex.watchstore.model.CartItem;
import com.avex.watchstore.model.Watch;
import com.avex.watchstore.repository.CartItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final WatchService watchService;

    public List<CartItem> getCartItems(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Transactional
    public CartItem addItem(String userId, Long watchId, Integer quantity) {
        // Validate the watch exists and has sufficient stock
        Watch watch = watchService.getWatchById(watchId);
        if (watch.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for watch: " + watch.getName());
        }

        // If the item already exists in cart, update quantity
        List<CartItem> existing = cartItemRepository.findByUserId(userId);
        for (CartItem item : existing) {
            if (item.getWatchId().equals(watchId)) {
                int newQty = item.getQuantity() + quantity;
                if (watch.getStockQuantity() < newQty) {
                    throw new IllegalArgumentException("Insufficient stock for watch: " + watch.getName());
                }
                item.setQuantity(newQty);
                return cartItemRepository.save(item);
            }
        }

        CartItem cartItem = CartItem.builder()
                .userId(userId)
                .watchId(watchId)
                .quantity(quantity)
                .build();
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public CartItem updateItemQuantity(String userId, Long itemId, Integer quantity) {
        CartItem item = cartItemRepository.findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with id: " + itemId));

        Watch watch = watchService.getWatchById(item.getWatchId());
        if (watch.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for watch: " + watch.getName());
        }

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Transactional
    public void removeItem(String userId, Long itemId) {
        CartItem item = cartItemRepository.findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with id: " + itemId));
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
