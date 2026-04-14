package com.salesavvy.app.entites;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int productId;                          // ✅ was product_id

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @Column(nullable = false)
    int stock;

    @Column(nullable = false)
    LocalDateTime createdAt;               // ✅ was created_at

    @Column(nullable = false)
    LocalDateTime updatedAt;               // ✅ was updated_at

    @ManyToOne
    @JoinColumn(name = "category_id")
    Categories category;                   // ✅ was catagories (also fixed typo)

    public Products() {}

    public Products(String name, String description, BigDecimal price, int stock, Categories category) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.category = category;
	}

	public Products(String name, String description, BigDecimal price, int stock,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Products(int productId, String name, String description, BigDecimal price,
                    int stock, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Products(int productId, String name, String description, BigDecimal price,
                    int stock, LocalDateTime createdAt, LocalDateTime updatedAt, Categories category) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Categories getCategory() { return category; }
    public void setCategory(Categories category) { this.category = category; }

    @Override
    public int hashCode() {
        return Objects.hash(category, createdAt, description, name, price, productId, stock, updatedAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Products other = (Products) obj;
        return productId == other.productId
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description)
                && Objects.equals(price, other.price)
                && stock == other.stock
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(updatedAt, other.updatedAt)
                && Objects.equals(category, other.category);
    }

    @Override
    public String toString() {
        return "Products [productId=" + productId + ", name=" + name + ", price=" + price
                + ", stock=" + stock + "]";
    }
}