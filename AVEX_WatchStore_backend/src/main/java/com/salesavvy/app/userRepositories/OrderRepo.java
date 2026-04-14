package com.salesavvy.app.userRepositories;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.salesavvy.app.entites.Order;
import com.salesavvy.app.entites.OrderStatus;



@Repository
public interface OrderRepo extends JpaRepository<Order, String>{

	Optional<Order> findById(int orderid);
	Optional<Order> findById(String orderId);
	
	 @Query("SELECT o FROM Order o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year AND o.status = com.salesavvy.app.entites.OrderStatus.SUCCESS")
	    List<Order> findSuccessfulOrdersByMonthAndYear(@Param("month") int month, @Param("year") int year);

	    // ✅ createdAt is LocalDateTime — use CAST to compare with LocalDate
	    @Query("SELECT o FROM Order o WHERE CAST(o.createdAt AS date) = :date AND o.status = com.salesavvy.app.entites.OrderStatus.SUCCESS")
	    List<Order> findSuccessfulOrdersByDate(@Param("date") LocalDate date);

	    // ✅ Find SUCCESS orders by year only
	    @Query("SELECT o FROM Order o WHERE YEAR(o.createdAt) = :year AND o.status = com.salesavvy.app.entites.OrderStatus.SUCCESS")
	    List<Order> findSuccessfulOrdersByYear(@Param("year") int year);

	    // ✅ Sum of all SUCCESS order amounts
	    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = com.salesavvy.app.entites.OrderStatus.SUCCESS")
	    BigDecimal calculateOverallBusiness();

	    // ✅ Find all orders by status — uses OrderStatus enum (not String)
	    @Query("SELECT o FROM Order o WHERE o.status = :status")
	    List<Order> findAllByStatus(@Param("status") OrderStatus status);
	
	
}
