package com.salesavvy.app.entites;

import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    // ✅ Fix 1: ManyToOne — many cart items belong to ONE user
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    // ✅ Fix 2: ManyToOne — many cart items can have ONE product
    @ManyToOne
    @JoinColumn(name = "product_id")
    Products product;

    @Column(name = "quantity")
    int quantity;

    public CartItem() {}

    public CartItem(int id, User user, Products product, int quantity) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public CartItem(User user, Products product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Products getProduct() { return product; }
    public void setProduct(Products product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, quantity, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem other = (CartItem) obj;
        return id == other.id
                && Objects.equals(product, other.product)
                && quantity == other.quantity
                && Objects.equals(user, other.user);
    }

    @Override
    public String toString() {
        return "CartItem [id=" + id + ", user=" + user + ", product=" + product + ", quantity=" + quantity + "]";
    }
}