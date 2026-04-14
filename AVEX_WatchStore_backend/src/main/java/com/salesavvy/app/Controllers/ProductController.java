package com.salesavvy.app.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.salesavvy.app.entites.Categories;
import com.salesavvy.app.entites.Products;
import com.salesavvy.app.entites.User;
import com.salesavvy.app.userRepositories.Categories_repo;
import com.salesavvy.app.userRepositories.Products_repo;
import com.salesavvy.app.userServices.ProductsServiceContract;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/products")
public class ProductController {


	ProductsServiceContract pro_service;
	private final Products_repo productsRepo;
	private final Categories_repo categoriesRepo;
	
	
	
	
	
	public ProductController(ProductsServiceContract pro_service, Products_repo products_repo, Categories_repo categoriesRepo) {
		super();
		this.pro_service = pro_service;
		this.productsRepo = products_repo;
		this.categoriesRepo = categoriesRepo;
	}




@GetMapping()
	public ResponseEntity<Map<String,Object>> getProducts(@RequestParam(required = false) String Catagory_name,HttpServletRequest request) {
	
		try {
		Map<String,Object> response = new HashMap<String, Object>();
		User user = (User) request.getAttribute("Authenticated_User");
		if(user != null) {
			// build user info when available, but do not block the public catalog
			response.put("User", user.getUsername());
			response.put("Role", user.getRole().name());
		}
		// get Products list
		List<Products> allproduct = pro_service.getProductbyCategories(Catagory_name);
		List<Map<String,Object>> productList = new ArrayList<>();
		for(Products product : allproduct) {
			Map<String, Object> productDetails = new HashMap<String, Object>(); 
			productDetails.put("product_id", product.getProductId());
			productDetails.put("name", product.getName());
			productDetails.put("price", product.getPrice());
			productDetails.put("description", product.getDescription());
			productDetails.put("stock", product.getStock());
			productDetails.put("Catagory_name", product.getCategory() != null ? product.getCategory().getCategory_name() : "Uncategorized");
			List<String> img_list = pro_service.getImageUrl(product.getProductId());
			productDetails.put("images",img_list);
			productList.add(productDetails);
			
		}
		response.put("Products", productList);
		return ResponseEntity.ok(response);
		
		}
		catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("message" ,e.getMessage()));
		}
		
		
	}

	@GetMapping("/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable Integer productId) {
		Optional<Products> productOpt = productsRepo.findById(productId);
		if (productOpt.isEmpty()) {
			return ResponseEntity.status(404).body(Map.of("message", "Product not found"));
		}

		Products product = productOpt.get();
		Map<String, Object> productDetails = new HashMap<>();
		productDetails.put("product_id", product.getProductId());
		productDetails.put("name", product.getName());
		productDetails.put("price", product.getPrice());
		productDetails.put("description", product.getDescription());
		productDetails.put("stock", product.getStock());
		productDetails.put("Catagory_name", product.getCategory() != null ? product.getCategory().getCategory_name() : "Uncategorized");
		productDetails.put("images", pro_service.getImageUrl(product.getProductId()));
		return ResponseEntity.ok(productDetails);
	}
	
}
