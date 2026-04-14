package com.salesavvy.app.userServiceImplementaion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.salesavvy.app.entites.Categories;
import com.salesavvy.app.entites.ProductImage;
import com.salesavvy.app.entites.Products;
import com.salesavvy.app.userRepositories.Categories_repo;
import com.salesavvy.app.userRepositories.ImageRepo;
import com.salesavvy.app.userRepositories.Products_repo;
import com.salesavvy.app.userServices.ProductsServiceContract;

@Service
public class ProductService implements ProductsServiceContract {

	Categories_repo category_repo;
	ImageRepo img_repo;
	Products_repo product_repo;
	
	public ProductService(Categories_repo category_repo, ImageRepo img_repo, Products_repo product_repo) {
		super();
		this.category_repo = category_repo;
		this.img_repo = img_repo;
		this.product_repo = product_repo;
	}

	@Override
	public List<Products> getProductbyCategories(String categories_name) {
		if (categories_name != null && !categories_name.isEmpty()) {
		Optional<Categories> category_opt = category_repo.findByCategoryNameIgnoreCase(categories_name.trim());
		if(category_opt.isPresent()) {
			Categories catogory = category_opt.get();
			return product_repo.findByCategory(catogory);
		}
		else {
			return product_repo.findAll();
		}
		} else {
		return product_repo.findAll();
		}
		
	}

	@Override
	public List<String> getImageUrl(Integer product_Id) {
		List<String> imageURL = new ArrayList<String>();
		List<ProductImage> img_objs = img_repo.findByProductProductId(product_Id);
		for(ProductImage image : img_objs ) {
			imageURL.add(image.getImageUrl());
		}
		
		return imageURL;
	}
	
	
	
	
}
