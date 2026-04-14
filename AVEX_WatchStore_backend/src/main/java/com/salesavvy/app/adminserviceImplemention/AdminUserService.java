package com.salesavvy.app.adminserviceImplemention;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesavvy.app.adminservice.AdminUserContract;
import com.salesavvy.app.entites.Role;
import com.salesavvy.app.entites.User;
import com.salesavvy.app.userRepositories.JwtRepo;
import com.salesavvy.app.userRepositories.UserRepo;

@Service
public class AdminUserService  implements AdminUserContract{

	
    private final UserRepo userrepo;
private final JwtRepo jwtrepo;
	


	public AdminUserService( UserRepo userrepo, JwtRepo jwtrepo) {
	

	this.userrepo = userrepo;
	this.jwtrepo = jwtrepo;
}

	@Override
	@Transactional
	public User modifyuser(int userId, String username, String email, String role) {
		User existingUser = userrepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found to update with this Id " + userId));
		
		if(username != null && !username.isEmpty()) {
			existingUser.setUsername(username);
		}
		if(email != null && !email.isEmpty()) {
			existingUser.setEmail(email);
		}
		if(role != null && !role.isEmpty())
		{try {
			existingUser.setRole(Role.valueOf(role));
		} catch (IllegalArgumentException e) {
	
			throw new IllegalArgumentException("Invaild role" + role);
		}
			
		}
		jwtrepo.deleteByUserId(userId);
		return userrepo.save(existingUser);
		
	
	}

	@Override
	public User getuserbyId(int userId) {

		return userrepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with this Id " + userId));
	}
	
	
}
