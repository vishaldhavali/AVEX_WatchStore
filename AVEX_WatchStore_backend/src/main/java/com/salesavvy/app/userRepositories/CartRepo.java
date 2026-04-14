package com.salesavvy.app.userRepositories;

import java.util.List;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.salesavvy.app.entites.CartItem;
import com.salesavvy.app.entites.Products;
import com.salesavvy.app.entites.User;

import jakarta.transaction.Transactional;

public interface CartRepo extends JpaRepository<CartItem, Integer> {

    // ✅ Removed @Query — findBy method handles this cleanly
    Optional<CartItem> findByUserAndProduct(User user, Products product);

    // ✅ Get all cart items for a user
    List<CartItem> findByUser(User user);
    @Modifying
    @Transactional
    void deleteByUserUserId(int userId);
    List<CartItem> findByUserUserId(int userId); 
    
    @Modifying
    @Transactional
    void deleteByUserUserIdAndProductProductId(int userId, int productId);
    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItem c WHERE c.user.userId = :userId")
    int countTotalItems(int userId);
}