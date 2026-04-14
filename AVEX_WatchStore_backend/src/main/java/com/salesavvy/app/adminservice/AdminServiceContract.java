package com.salesavvy.app.adminservice;

import com.salesavvy.app.entites.Products;

public interface AdminServiceContract {
	public Products addProductWithImage(String name, String description, Double price, Integer Stock,
			Integer catagoryId, String imgUrl);

	public void deleteProduct(Integer productId);

}
