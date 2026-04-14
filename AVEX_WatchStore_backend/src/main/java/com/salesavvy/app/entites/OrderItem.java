package com.salesavvy.app.entites;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")

public class OrderItem {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private	int id;

@ManyToOne()
@JoinColumn(name = "order_id", nullable = false)
private Order order;

@Column(name = "product_id" , nullable = false)	
private	int productId;
@Column(name = "quantity",nullable =false)
private int quantity;
@Column(name = "price_per_unit",nullable =false)
private BigDecimal pricePerUnite;
@Column(name = "total_price",nullable = false)
private BigDecimal totalPrice;
	
public OrderItem() {
}

public OrderItem(int id, Order order, int productId, int quantity, BigDecimal pricePerUnite, BigDecimal totalPrice) {
	super();
	this.id = id;
	this.order = order;
	this.productId = productId;
	this.quantity = quantity;
	this.pricePerUnite = pricePerUnite;
	this.totalPrice = totalPrice;
}

public OrderItem(Order order, int productId, int quantity, BigDecimal pricePerUnite, BigDecimal totalPrice) {
	super();
	this.order = order;
	this.productId = productId;
	this.quantity = quantity;
	this.pricePerUnite = pricePerUnite;
	this.totalPrice = totalPrice;
}

public OrderItem(int productId, int quantity, BigDecimal pricePerUnite, BigDecimal totalPrice) {
	super();
	this.productId = productId;
	this.quantity = quantity;
	this.pricePerUnite = pricePerUnite;
	this.totalPrice = totalPrice;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public Order getOrder() {
	return order;
}

public void setOrder(Order order) {
	this.order = order;
}

public int getProductId() {
	return productId;
}

public void setProductId(int productId) {
	this.productId = productId;
}

public int getQuantity() {
	return quantity;
}

public void setQuantity(int quantity) {
	this.quantity = quantity;
}

public BigDecimal getPricePerUnite() {
	return pricePerUnite;
}

public void setPricePerUnite(BigDecimal bigDe) {
	this.pricePerUnite = bigDe;
}

public BigDecimal getTotalPrice() {
	return totalPrice;
}

public void setTotalPrice(BigDecimal totalPrice) {
	this.totalPrice = totalPrice;
}

@Override
public int hashCode() {
	return Objects.hash(id, order, pricePerUnite, productId, quantity, totalPrice);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	OrderItem other = (OrderItem) obj;
	return id == other.id && Objects.equals(order, other.order) && pricePerUnite == other.pricePerUnite
			&& productId == other.productId && quantity == other.quantity
			&& Objects.equals(totalPrice, other.totalPrice);
}

@Override
public String toString() {
	return "OrderItem [id=" + id + ", order=" + order + ", productId=" + productId + ", quantity=" + quantity
			+ ", pricePerUnite=" + pricePerUnite + ", totalPrice=" + totalPrice + "]";
}


}