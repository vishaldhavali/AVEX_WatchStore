package com.salesavvy.app.userServices;

import java.util.List;

import com.salesavvy.app.entites.Products;

public interface ProductsServiceContract {

public 	List<Products> getProductbyCategories(String categories_name);
public 	List<String> getImageUrl(Integer product_Id); 
}
