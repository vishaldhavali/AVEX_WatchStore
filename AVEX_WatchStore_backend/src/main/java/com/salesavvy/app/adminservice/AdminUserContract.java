package com.salesavvy.app.adminservice;

import com.salesavvy.app.entites.User;

public interface AdminUserContract {

	public User modifyuser(int userId,String username,String email,String role );
	public User getuserbyId(int userId);
}
