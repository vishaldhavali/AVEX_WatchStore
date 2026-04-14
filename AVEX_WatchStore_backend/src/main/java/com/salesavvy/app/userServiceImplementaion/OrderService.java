package com.salesavvy.app.userServiceImplementaion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.salesavvy.app.entites.OrderItem;
import com.salesavvy.app.entites.ProductImage;
import com.salesavvy.app.entites.Products;
import com.salesavvy.app.entites.User;
import com.salesavvy.app.userRepositories.ImageRepo;
import com.salesavvy.app.userRepositories.OrderItemRepo;
import com.salesavvy.app.userRepositories.Products_repo;
import com.salesavvy.app.userServices.OrderServiceContract;
@Service
public class OrderService implements OrderServiceContract {

	private final OrderItemRepo orderitemrepo;
	private final Products_repo productrepo;
	private final ImageRepo imgrepo;
	
	
	
	
	
	public OrderService(OrderItemRepo orderitemrepo, Products_repo productrepo, ImageRepo imgrepo) {
		super();
		this.orderitemrepo = orderitemrepo;
		this.productrepo = productrepo;
		this.imgrepo = imgrepo;
	}





	@Override
	public Map<String, Object> getOrderForUser(User user) {
		List<OrderItem> sucessfulorder = orderitemrepo.findSuccessfulOrderItemsByUserId(user.getUserId());
		
		Map<String,Object> response = new HashMap<String, Object>();
		response.put("username", user.getUsername());
		response.put("role", user.getRole().name());
		
		List<Map<String,Object>> products = new ArrayList<Map<String,Object>>();
		
		for(OrderItem orderitem:sucessfulorder) {
			Optional<Products> productOpt = productrepo.findById(orderitem.getProductId());
			if (productOpt.isEmpty()) {
				continue;
			}
			Products product = productOpt.get();
			List<ProductImage> pimgs = imgrepo.findByProductProductId(product.getProductId());
			String url = pimgs.isEmpty() ? null : pimgs.get(0).getImageUrl();
			Map<String, Object> productdetails = new HashMap<String, Object>();
			productdetails.put("order_id", orderitem.getId());
			productdetails.put("quantity", orderitem.getQuantity());
			productdetails.put("total_price", orderitem.getTotalPrice());
			productdetails.put("image", url);
			productdetails.put("product_id", product.getProductId());
			productdetails.put("name", product.getName());
			productdetails.put("description", product.getDescription());
			productdetails.put("price_per_unit", product.getPrice());
			
			
			products.add(productdetails);
			
			
		}
		response.put("Products", products);
		
		
		return response;
	}

}
