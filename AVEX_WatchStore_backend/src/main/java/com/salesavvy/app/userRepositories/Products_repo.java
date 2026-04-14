package com.salesavvy.app.userRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.salesavvy.app.entites.Categories;
import com.salesavvy.app.entites.Products;

@Repository
public interface Products_repo extends JpaRepository<Products, Integer> {

    // ✅ Pass Categories object, not Integer
    List<Products> findByCategory(Categories category);
    @Query("SELECT p.category.categoryName FROM Products p WHERE p.productId = :productId")
    String findCategoryNameByProductId(@Param("productId") int productId);
}