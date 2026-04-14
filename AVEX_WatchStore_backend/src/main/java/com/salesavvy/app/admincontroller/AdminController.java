package com.salesavvy.app.admincontroller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesavvy.app.adminservice.AdminServiceContract;
import com.salesavvy.app.entites.Products;

@RestController
@RequestMapping("/Admin/products")
public class AdminController {

	

	private final AdminServiceContract adminService;

	public AdminController(AdminServiceContract adminService) {
		super();
		this.adminService = adminService;
		
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addProduct(@RequestBody Map<String,Object> productrequest) {
		try {
			String name =(String) productrequest.get("name");
			String description =(String) productrequest.get("description");
			Double price = Double.valueOf(String.valueOf(productrequest.get("price")));
			Integer stock = (Integer) productrequest.get("stock");
			Integer catagoryId = (Integer) productrequest.get("catagoryId");
			String imgurl = (String) productrequest.get("imageUrl");
			
			Products addedProduct = adminService.addProductWithImage(name, description, price, stock, catagoryId, imgurl);
			return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
			
			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error : " + e.getMessage());
			
		} catch (Exception e ) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
		}

		
	}
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteProduct(@RequestBody Map<String,Object> request) {
		
		try {
			Integer productId = (Integer)request.get("productId");
			adminService.deleteProduct(productId);
			return ResponseEntity.status(HttpStatus.OK).body("Product Deleted Successfully ");
			
		} catch (IllegalArgumentException e) {
			
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error " + e.getMessage() );
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error " + e.getMessage() );
		}

	}
}
