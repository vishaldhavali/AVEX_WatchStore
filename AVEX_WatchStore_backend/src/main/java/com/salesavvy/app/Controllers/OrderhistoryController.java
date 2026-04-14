package com.salesavvy.app.Controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesavvy.app.entites.User;
import com.salesavvy.app.userServices.OrderServiceContract;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order")
public class OrderhistoryController {
	private OrderServiceContract orderServiecHtry;

	public OrderhistoryController(OrderServiceContract orderServiecHtry) {
		super();
		this.orderServiecHtry = orderServiecHtry;
	}
	
	
	@GetMapping("/orderhistory")
	public ResponseEntity<Map<String, Object>> getUserorderHistory(HttpServletRequest request) {
	try {
		User user = (User)	request.getAttribute("Authenticated_User");
	if(user == null) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error  ","User is UnAuthorized for this Order History"));
	}
	
	Map<String, Object>  response = orderServiecHtry.getOrderForUser(user);
	return ResponseEntity.ok(response);
	}
	catch (IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Error " , e.getMessage()));
	} catch (Exception e) {
	    // Handle unexpected exceptions
	    e.printStackTrace();
	    return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred"));
	}
	
	}
}
