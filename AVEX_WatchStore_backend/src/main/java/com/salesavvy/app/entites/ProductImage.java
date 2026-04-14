package com.salesavvy.app.entites;

import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "productimages")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int imageId;                         // ✅ was image_id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Products product;

    @Column(name = "image_url")
    String imageUrl;                     // ✅ was image_url

    public ProductImage() {}

    public ProductImage(int imageId, Products product, String imageUrl) {
        this.imageId = imageId;
        this.product = product;
        this.imageUrl = imageUrl;
    }

    public ProductImage(Products product, String imageUrl) {
        this.product = product;
        this.imageUrl = imageUrl;
    }

    public int getImageId() { return imageId; }
    public void setImageId(int imageId) { this.imageId = imageId; }

    public Products getProduct() { return product; }
    public void setProduct(Products product) { this.product = product; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, imageUrl, product);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductImage other = (ProductImage) obj;
        return imageId == other.imageId
                && Objects.equals(imageUrl, other.imageUrl)
                && Objects.equals(product, other.product);
    }

    @Override
    public String toString() {
        return "ProductImage [imageId=" + imageId + ", imageUrl=" + imageUrl + "]";
    }
}