package com.salesavvy.app.userServices;

import java.util.Map;

import com.salesavvy.app.entites.User;

public interface OrderServiceContract {

	public Map<String,Object> getOrderForUser(User user);
}
