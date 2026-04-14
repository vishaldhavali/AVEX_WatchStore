package com.salesavvy.app.entites;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	
	
	@Id
	@Column(name = "order_id")
	private String id;

@Column(name = "user_id")
int userId;

@Column(name = "total_amount")
BigDecimal totalAmount;

@Enumerated(EnumType.STRING)
@Column(name = "status")
private OrderStatus status;

@Column(name = "created_at",nullable = false,updatable = false)
private LocalDateTime createdAt;

@Column(name = "updated_at")
private LocalDateTime updatedAt;

@OneToMany(mappedBy = "order")
List<OrderItem> orderitems; 

public Order() {

}

public Order(String id, int userId, BigDecimal totalAmount, OrderStatus status, LocalDateTime createdAt,
		LocalDateTime updatedAt, List<OrderItem> orderitems) {
	super();
	this.id = id;
	this.userId = userId;
	this.totalAmount = totalAmount;
	this.status = status;
	this.createdAt = createdAt;
	this.updatedAt = updatedAt;
	this.orderitems = orderitems;
}

public Order(int userId, BigDecimal totalAmount, OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt,
		List<OrderItem> orderitems) {
	super();
	this.userId = userId;
	this.totalAmount = totalAmount;
	this.status = status;
	this.createdAt = createdAt;
	this.updatedAt = updatedAt;
	this.orderitems = orderitems;
}

public Order(int userId, BigDecimal totalAmount, OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
	super();
	this.userId = userId;
	this.totalAmount = totalAmount;
	this.status = status;
	this.createdAt = createdAt;
	this.updatedAt = updatedAt;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public int getUserId() {
	return userId;
}

public void setUserId(int userId) {
	this.userId = userId;
}

public BigDecimal getTotalAmount() {
	return totalAmount;
}

public void setTotalAmount(BigDecimal totalAmount) {
	this.totalAmount = totalAmount;
}

public OrderStatus getStatus() {
	return status;
}

public void setStatus(OrderStatus status) {
	this.status = status;
}

public LocalDateTime getCreatedAt() {
	return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
}

public LocalDateTime getUpdatedAt() {
	return updatedAt;
}

public void setUpdatedAt(LocalDateTime updatedAt) {
	this.updatedAt = updatedAt;
}

public List<OrderItem> getOrderitems() {
	return orderitems;
}

public void setOrderitems(List<OrderItem> orderitems) {
	this.orderitems = orderitems;
}

@Override
public int hashCode() {
	return Objects.hash(createdAt, id, orderitems, status, totalAmount, updatedAt, userId);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Order other = (Order) obj;
	return Objects.equals(createdAt, other.createdAt) && id == other.id && Objects.equals(orderitems, other.orderitems)
			&& status == other.status && Objects.equals(totalAmount, other.totalAmount)
			&& Objects.equals(updatedAt, other.updatedAt) && userId == other.userId;
}

@Override
public String toString() {
	return "Order [id=" + id + ", userId=" + userId + ", totalAmount=" + totalAmount + ", status=" + status
			+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", orderitems=" + orderitems + "]";
}


}
