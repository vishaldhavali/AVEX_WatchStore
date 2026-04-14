package com.salesavvy.app.userRepositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import com.salesavvy.app.entites.ProductImage;
import com.salesavvy.app.entites.Products;

import jakarta.transaction.Transactional;

@Repository
public interface ImageRepo extends JpaRepository<ProductImage, Integer> {

    // ✅ Find images by the product object
    List<ProductImage> findByProduct(Products product);

    // ✅ OR find images by product's ID (Spring navigates product.productId)
    List<ProductImage> findByProductProductId(int productId);
    
    @Modifying
    @Transactional
    void deleteByProductProductId(int productId);
}