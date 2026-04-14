package com.salesavvy.app.userServices;


import java.util.Map;

import com.salesavvy.app.entites.User;

public interface CartServiceContract {

	public void addToCart(User user, int productId, int Qty) ;
	public Map<String,Object> getCartItems(int user_id);
	public void cartUpdateQuantity(User user,int productid,int qty);
	public void deleteCartiteam(int userId, int productId) ;
	public int CountofItems(int userId);
}
