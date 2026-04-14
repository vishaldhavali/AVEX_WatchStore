package com.salesavvy.app.Controllers;

import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesavvy.app.entites.User;
import com.salesavvy.app.userServices.CartServiceContract;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartServiceContract cservice;
	

	public CartController(CartServiceContract cservice) {

		this.cservice = cservice;
		
	}

	@PostMapping("/items")
	public ResponseEntity<Map<String, Object>> addtoCartItem(@RequestBody Map<String, Object> request,
			HttpServletRequest req) {
		User user = (User) req.getAttribute("Authenticated_User");
		int productid = toInt(request.get("productId"));
		int quantity = request.containsKey("quantity") ? toInt(request.get("quantity")) : 1;
		cservice.addToCart(user, productid, quantity);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/items")
	public ResponseEntity<Map<String, Object>> getCartIteams(HttpServletRequest request) {
		User user = (User) request.getAttribute("Authenticated_User");
		Map<String, Object> cartitems = cservice.getCartItems(user.getUserId());
		return ResponseEntity.ok(cartitems);
	}

	@PutMapping("/update")
	public ResponseEntity<Void> updateCartItems(@RequestBody Map<String, Object> req, HttpServletRequest request) {
		int productid = toInt(req.get("productId"));
		int qty = toInt(req.get("quantity"));
		User user = (User) request.getAttribute("Authenticated_User");
		cservice.cartUpdateQuantity(user, productid, qty);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteCartItems(@RequestBody Map<String, Object> req, HttpServletRequest request) {
		int productid = toInt(req.get("productId"));
		User user = (User) request.getAttribute("Authenticated_User");
		cservice.deleteCartiteam(user.getUserId(), productid);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	private int toInt(Object value) {
		if (value instanceof Number number) {
			return number.intValue();
		}
		if (value instanceof String str) {
			return Integer.parseInt(str.trim());
		}
		throw new IllegalArgumentException("Invalid numeric value: " + value);
	}

	@GetMapping("/totalcount")
	public ResponseEntity<Integer> getCartItemCount(HttpServletRequest request) {
		User user = (User) request.getAttribute("Authenticated_User");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0);
		}
		int count = cservice.CountofItems(user.getUserId());
		return ResponseEntity.ok(count);
	}
}
