package com.salesavvy.app.userServices;

import com.salesavvy.app.entites.User;

public interface UserAuth {

	User authicate(String username,String password) throws Exception; 
	public String generatetoken(User user);
	public boolean verifyToken(String token);
	public String extractusername(String token);
	public void logout(User user);
	
}
