package com.salesavvy.app.userServiceImplementaion;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.salesavvy.app.entites.CartItem;
import com.salesavvy.app.entites.ProductImage;
import com.salesavvy.app.entites.Products;
import com.salesavvy.app.entites.User;
import com.salesavvy.app.userRepositories.CartRepo;
import com.salesavvy.app.userRepositories.ImageRepo;
import com.salesavvy.app.userRepositories.Products_repo;
import com.salesavvy.app.userRepositories.UserRepo;
import com.salesavvy.app.userServices.CartServiceContract;

@Service
public class CartService implements CartServiceContract {

    private final Products_repo productRepo;   // ✅ use correct repo name
    private final CartRepo cartRepo;
    private final ImageRepo imgrepo;
    private final UserRepo userrepo;

    public CartService(Products_repo productRepo, CartRepo cartRepo,ImageRepo imgrepo,UserRepo userrepo) {
        this.productRepo = productRepo;
        this.cartRepo = cartRepo;
        this.imgrepo = imgrepo;
        this.userrepo = userrepo;
    }

    @Override
    public void addToCart(User user, int productId, int qty) {
        Products product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // ✅ Fixed method name to match CartRepo
        Optional<CartItem> existingItem = cartRepo.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            CartItem oldItem = existingItem.get();
            oldItem.setQuantity(oldItem.getQuantity() + qty);  // ✅ add qty
            cartRepo.save(oldItem);
        } else {
            CartItem newItem = new CartItem(user, product, qty);
            cartRepo.save(newItem);
        }
    }

	@Override
	public Map<String, Object> getCartItems(int user_id) {
		List<CartItem> alliteams = cartRepo.findByUserUserId(user_id);
		User user = userrepo.findById(user_id).orElseThrow(() -> new IllegalArgumentException("Message : User not found"));
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("User", user.getUsername());
		response.put("Role", user.getRole().name());
		// fetch all products
		double overallprice = 0;
		List<Map<String, Object>> cartWithItems = new ArrayList<>();
		for(CartItem items : alliteams) {
			Map<String, Object> productdetails = new HashMap<>();			
			Products products = productRepo.findById(items.getProduct().getProductId())
				    .orElseThrow(() -> new RuntimeException("Product not found: " + items.getProduct().getProductId()));			List<ProductImage> images = imgrepo.findByProductProductId(products.getProductId()) ;
			String imgurl = null;
			if(images != null && !images.isEmpty()) {
			imgurl = images.get(0).getImageUrl();
		} else {
			imgurl = "default-img-url";
		}
			
			productdetails.put("productId",products.getProductId());
			productdetails.put("imageUrl", imgurl);
			productdetails.put("name", products.getName());
			productdetails.put("description", products.getDescription());
			productdetails.put("price_per_unit", products.getPrice());
			productdetails.put("quantity", items.getQuantity());
			productdetails.put("total_price", items.getQuantity() * products.getPrice().doubleValue());
			
	cartWithItems.add(productdetails);
	overallprice += items.getQuantity()*products.getPrice().doubleValue();
	
		
		}
		
		Map<String, Object> cart = new HashMap<String, Object>();
		cart.put("Product", cartWithItems);
		cart.put("overallprice", overallprice);
		cart.put("totalItems", alliteams.size()); 
		response.put("cart", cart);
		return response;
	}
    
    
	public void cartUpdateQuantity(User user,int productid,int qty) {
		Products product = productRepo.findById(productid).orElseThrow(() -> new IllegalArgumentException("Product not found"));
		
	Optional	<CartItem> existingiteam = cartRepo.findByUserAndProduct(user, product);
	if(existingiteam.isPresent()) {
	 CartItem cartitem = existingiteam.get();
	 if(qty == 0) {
		 deleteCartiteam(user.getUserId(),product.getProductId());
	 } else {
		 cartitem.setQuantity(qty);
		 cartRepo.save(cartitem);
	 }
	}
		
	}

	public void deleteCartiteam(int userId, int productId) {
		
	// Products product = productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product you delte not found"));
	cartRepo.deleteByUserUserIdAndProductProductId(userId, productId);
	}
	
	public int CountofItems(int userId) {
		int count = cartRepo.countTotalItems(userId);
		return count;
	}
	
    
}