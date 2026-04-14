package com.salesavvy.app.userRepositories;

import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesavvy.app.entites.Categories;
@Repository
public interface Categories_repo extends JpaRepository<Categories, Integer> {
	Optional<Categories> findByCategoryName(String categoryName);
	Optional<Categories> findByCategoryNameIgnoreCase(String categoryName);
}
