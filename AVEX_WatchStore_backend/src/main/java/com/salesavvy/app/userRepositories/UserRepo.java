package com.salesavvy.app.userRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesavvy.app.entites.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);
	
}
