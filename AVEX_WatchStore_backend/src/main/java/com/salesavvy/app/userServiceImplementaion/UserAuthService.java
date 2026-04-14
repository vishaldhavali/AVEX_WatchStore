package com.salesavvy.app.userServiceImplementaion;

import java.nio.charset.StandardCharsets;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.salesavvy.app.entites.JwtToken;
import com.salesavvy.app.entites.User;
import com.salesavvy.app.userRepositories.JwtRepo;
import com.salesavvy.app.userRepositories.UserRepo;
import com.salesavvy.app.userServices.UserAuth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class UserAuthService implements UserAuth {


	private final JwtRepo jwtrepo;
	
	private final UserRepo userrepo;
	
	private final SecretKey Signinkey;
	
	private final BCryptPasswordEncoder encoder;
	 

	public UserAuthService(JwtRepo jwtrepo, UserRepo userrepo, BCryptPasswordEncoder encoder,
	        @Value("${jwt.secret}") String jwtkey) {
	    super();
	    this.jwtrepo = jwtrepo;
	    this.userrepo = userrepo;
	    this.encoder = encoder;

	    if (jwtkey.getBytes(StandardCharsets.UTF_8).length < 64) {
	        throw new IllegalArgumentException("Set jwt secret key above 64 byte");
	    }
	    this.Signinkey = Keys.hmacShaKeyFor(jwtkey.getBytes(StandardCharsets.UTF_8));
	}
	
	@Override
	public User authicate(String username, String password) throws Exception {

	    User user = userrepo.findByUsername(username)
	            .orElseThrow(() -> new Exception("Invalid user, try to signup"));

	    if (!encoder.matches(password, user.getPassword())) {
	        throw new Exception("Invalid password");
	    }

	    return user;
	}
	
	public String generatetoken(User user) {
		String token;
		LocalDateTime now = LocalDateTime.now();
		JwtToken existingToken = jwtrepo.findByUserId(user.getUserId());
		if(existingToken != null
				&& now.isBefore(existingToken.getExpires_at())
				&& isReusableJwt(existingToken.getToken())) {
			token = existingToken.getToken();
		} else {
			token = generateNewToken(user);
			if(existingToken != null) {
				jwtrepo.delete(existingToken);
			}
			saveToken(user,token);
		}
		return token;
	}

	private boolean isReusableJwt(String token) {
		if (token == null) {
			return false;
		}

		// Legacy hashed tokens have no dot separators and are not valid JWTs.
		if (token.chars().filter(ch -> ch == '.').count() != 2) {
			return false;
		}

		try {
			Jwts.parser().verifyWith(Signinkey).build().parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void saveToken(User user, String token) {
		LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
		// ✅ Store the actual JWT token in the database (not hashed)
		// Hashing is only for verification lookup purposes
		JwtToken jwtreg = new JwtToken(token, expiresAt, user);
		jwtrepo.save(jwtreg);
	}

	private String generateNewToken(User user) {
		
		return Jwts.builder()
                .subject(user.getUsername()) 
                .claim("role", user.getRole().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(Signinkey) 
                .compact();
		
	}
	
	public boolean verifyToken(String token) {
	    try {
	        System.out.println("Validating Token: " + token);

	        // Step 1: Verify signature + expiry (JJWT handles this automatically)
	        Claims claims = Jwts.parser()
	                .verifyWith(Signinkey)
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();

	        System.out.println("Subject: " + claims.getSubject());
	        System.out.println("Expiry from token: " + claims.getExpiration());

	        // Step 2: Check DB (for revocation / logout support)
	        // ✅ Now compare against stored JWT token directly (not hashed)
	        Optional<JwtToken> jwtToken = jwtrepo.findByToken(token);
	        if (jwtToken.isEmpty()) {
	            System.out.println("Token not found in DB (may be revoked/logged out)");
	            return false;
	        }

	        System.out.println("Token found in DB, is valid.");
	        return true;

	    } catch (ExpiredJwtException e) {
	        System.out.println("Token expired: " + e.getMessage());
	        return false;
	    }catch (Exception e) {
	    	System.out.println("Token Vaildation issue:  " + e.getMessage());
	        return false;
		}
	    
	}
	
	
	public String extractusername(String token) { 
		return Jwts.parser().verifyWith(Signinkey).build().parseSignedClaims(token).getPayload().getSubject();
	}

	@Override
	public void logout(User user) {
		int userid = user.getUserId();
		JwtToken token = jwtrepo.findByUserId(userid);
		if(token != null) {
			jwtrepo.deleteByUserId(userid);
		}
		
	}
	
	
	
	
}
