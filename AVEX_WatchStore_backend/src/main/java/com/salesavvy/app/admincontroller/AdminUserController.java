package com.salesavvy.app.admincontroller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.salesavvy.app.adminservice.AdminUserContract;
import com.salesavvy.app.entites.User;

@RestController
// FIX 1: Added leading slash — "/Admin/users" instead of "Admin/users".
// Without the slash, Spring resolves the path relative to the servlet context,
// which can cause 404s depending on the server setup.
// FIX 2: Path is now "/Admin/users" (capital A) which matches the
// AdminRoleGuard check in AuthenticationFilter: request_URI.startsWith("/Admin/").
@RequestMapping("/Admin/users")
public class AdminUserController {

    private final AdminUserContract adminuserService;

    public AdminUserController(AdminUserContract adminuserService) {
        super();
        this.adminuserService = adminuserService;
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modifyUser(@RequestBody Map<String, Object> requestbody) {
        try {
            Integer userid = (Integer) requestbody.get("userId");
            String username = (String) requestbody.get("username");
            String email = (String) requestbody.get("email");
            String role = (String) requestbody.get("role");

            User updateduser = adminuserService.modifyuser(userid, username, email, role);
            return ResponseEntity.status(HttpStatus.OK).body(updateduser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
        }
    }

    // FIX 3: Changed from @GetMapping + @RequestBody to @PostMapping.
    // HTTP GET requests with a request body are non-standard — many proxies,
    // load balancers, and HTTP clients silently drop the body on GET requests,
    // causing the userId to arrive as null. POST is the correct verb here.
    @PostMapping("/getbyid")
    public ResponseEntity<?> getUserByID(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            return ResponseEntity.status(HttpStatus.OK).body(adminuserService.getuserbyId(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
        }
    }
}
