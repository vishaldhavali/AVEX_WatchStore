package com.salesavvy.app.userRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.salesavvy.app.entites.JwtToken;

import jakarta.transaction.Transactional;
@Repository
public interface JwtRepo extends JpaRepository<JwtToken, Integer> {

	@Query("Select t from JwtToken t where t.user.userId = :userId") 
	JwtToken findByUserId(@Param("userId") int userId);
	 Optional<JwtToken> findByToken(String token);
	 
	 @Modifying
	 @Transactional
	 @Query("DELETE FROM JwtToken j WHERE j.user.userId = :userId")
	 void deleteByUserId(@Param("userId") int userId);

}
