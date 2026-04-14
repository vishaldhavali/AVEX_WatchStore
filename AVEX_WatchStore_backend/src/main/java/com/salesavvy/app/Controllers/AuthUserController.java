package com.salesavvy.app.Controllers;



import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.salesavvy.app.Dao.UserloginResponse;
import com.salesavvy.app.Dto.LoginRequest;
import com.salesavvy.app.entites.User;
import com.salesavvy.app.userServices.UserAuth;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://localhost:5167", allowCredentials = "true")
public class AuthUserController {

   
	private final UserAuth userauth;

    public AuthUserController(UserAuth userauth) {
        this.userauth = userauth;
		
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletResponse response) {
        try {
            User user = userauth.authicate(req.getUsername(), req.getPassword());
            String token = userauth.generatetoken(user);

            // ✅ Set JWT token in HttpOnly cookie for secure transport
            ResponseCookie cookie = ResponseCookie.from("AuthId", token)
                    .httpOnly(true)        // Prevent JS access (security)
                    .secure(false)         // localhost doesn't have HTTPS
                    .path("/")             // Available to all paths
                    .maxAge(3600)          // 1 hour expiration in seconds
                    .sameSite("Lax")       // Allow cross-site but with restrictions
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            return ResponseEntity.ok(Map.of(
                    "message", "Login Successful",
                    "user", new UserloginResponse(user.getUsername(), user.getRole().name())
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    
    @PostMapping("/logout") 
    public ResponseEntity<Map<String, String>> userlogout(HttpServletRequest request,HttpServletResponse response) {
    	try {
    		User user = (User) request.getAttribute("Authenticated_User");
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error","User not found logout"));
			}
			userauth.logout(user);
			ResponseCookie cookie = ResponseCookie.from("AuthId", null)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(0)          
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());
            return ResponseEntity.ok(Map.of("message", "Logout Successful"));
			
    	} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<String, String>();
			errorResponse.put("message", "Logout Failed");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
    }
}