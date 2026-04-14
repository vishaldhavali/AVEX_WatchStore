package com.salesavvy.app.adminserviceImplemention;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.salesavvy.app.adminservice.AdminServiceContract;
import com.salesavvy.app.entites.Categories;
import com.salesavvy.app.entites.ProductImage;
import com.salesavvy.app.entites.Products;
import com.salesavvy.app.userRepositories.Categories_repo;
import com.salesavvy.app.userRepositories.ImageRepo;
import com.salesavvy.app.userRepositories.Products_repo;

@Service
public class AdminServiceImp implements AdminServiceContract {

	Products_repo pro_repo;
	ImageRepo imgrepo;
	Categories_repo catrepo;
	
	
	
	
	public AdminServiceImp(Products_repo pro_repo, ImageRepo imgrepo, Categories_repo catrepo) {
		super();
		this.pro_repo = pro_repo;
		this.imgrepo = imgrepo;
		this.catrepo = catrepo;
	}

	@Override
	public Products addProductWithImage(String name, String description, Double price, Integer Stock,
			Integer catagoryId, String imgUrl) {
		Categories category = catrepo.findById(catagoryId).orElseThrow(() -> new IllegalArgumentException("Invaild Catagory id"));
		
		// Create prodsuct and save
		
		Products newproduct = new Products();
		newproduct.setCategory(category);
		newproduct.setName(name);
		newproduct.setPrice(new BigDecimal(price));
		newproduct.setStock(Stock);
		newproduct.setDescription(description);
		
		Products savedProducts = pro_repo.save(newproduct);
		if(imgUrl != null && !imgUrl.isEmpty()) {
			ProductImage productimg = new ProductImage();
			productimg.setProduct(savedProducts);
			productimg.setImageUrl(imgUrl);
			imgrepo.save(productimg);
		} else {
			throw new IllegalArgumentException("invaild Image URl");
		}
		
		return savedProducts;
	}

	@Override
	public void deleteProduct(Integer productId) {
		if(!pro_repo.existsById(productId)) {
			throw new IllegalArgumentException("Product not Found");
		}
		imgrepo.deleteByProductProductId(productId);
		pro_repo.deleteById(productId);
	}

}
