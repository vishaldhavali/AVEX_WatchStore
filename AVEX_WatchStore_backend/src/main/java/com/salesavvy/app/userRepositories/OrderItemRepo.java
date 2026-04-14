package com.salesavvy.app.userRepositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.salesavvy.app.entites.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {
	 List<OrderItem> findByOrder_Id(String orderId);
	 
	 @Query("SELECT oi FROM OrderItem oi WHERE oi.order.userId = :userId AND oi.order.status = 'SUCCESS'")
	 List<OrderItem> findSuccessfulOrderItemsByUserId(@Param("userId") int userId);
}
