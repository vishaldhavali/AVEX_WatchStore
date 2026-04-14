package com.salesavvy.app.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.salesavvy.app.entites.Role;
import com.salesavvy.app.entites.User;
import com.salesavvy.app.userRepositories.UserRepo;
import com.salesavvy.app.userServiceImplementaion.UserAuthService;
import com.salesavvy.app.userServices.UserAuth;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final UserAuth service;
    private final UserRepo userrepo;

    private final String ALLOWED_ORIGIN = "http://localhost:5167";

    private final String[] UNAUTH_PATH = {
        "/api/users/login",
        "/api/users/register",
        // FIX: Added OTP endpoints — these must be public so unauthenticated users
        // (e.g. during registration or password reset) can request and verify OTPs.
        "/api/users/send-otp",
        "/api/users/verify-otp",
        "/api/products",
        // Product detail route (/api/products/{id}) is also public — matches via startsWith
        "/api/products/"
    };

    public AuthenticationFilter(UserAuthService service, UserRepo userrepo) {
        System.out.println("Filter Started");
        this.service = service;
        this.userrepo = userrepo;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            executeFilterlogic(request, response, chain);
        } catch (Exception e) {
            logger.error("Error in executeFilterlogic() from AuthenticationFilter", e);
            sendErrorResponse((HttpServletResponse) response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    private void executeFilterlogic(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http_request = (HttpServletRequest) request;
        HttpServletResponse http_response = (HttpServletResponse) response;

        // Set CORS headers for ALL requests
        setCORSHeaders(http_response);

        String request_URI = http_request.getRequestURI();
        logger.info("Request URI : {}", request_URI);

        boolean isUnauth = Arrays.stream(UNAUTH_PATH)
                .anyMatch(path -> request_URI.startsWith(path));

        if (isUnauth) {
            chain.doFilter(request, response);
            return;
        }

        // FIX: Handle OPTIONS preflight BEFORE token check — browsers send OPTIONS
        // without cookies, which caused all CORS preflight requests to return 401.
        // Moved this block above the token validation.
        if (http_request.getMethod().equalsIgnoreCase("OPTIONS")) {
            setCORSHeaders(http_response);
            return;
        }

        String token = getAuthTokenFromCookies(http_request);

        if (token == null || !service.verifyToken(token)) {
            sendErrorResponse(http_response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized Token Try to Re - login");
            return;
        }

        String username = service.extractusername(token);
        Optional<User> user = userrepo.findByUsername(username);

        if (user.isEmpty()) {
            sendErrorResponse(http_response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized : User not Found");
            return;
        }

        User auth_user = user.get();
        String role = auth_user.getRole().toString();
        logger.info("Authenticated User : {} and Role : {}", auth_user.getUsername(), role);

        // FIX: Admin path check changed from "/admin/" to "/Admin/" to match the
        // actual @RequestMapping("Admin/users") in AdminUserController.
        // Original code used lowercase "/admin/" so the role guard was NEVER triggered,
        // meaning any authenticated user could reach admin endpoints.
        if (request_URI.startsWith("/Admin/") && !role.equals(Role.ADMIN.toString())) {
            sendErrorResponse(http_response, HttpServletResponse.SC_FORBIDDEN,
                    "Forbidden : Admin Access Only");
            return;
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(auth_user.getUsername(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        http_request.setAttribute("Authenticated_User", auth_user);
        chain.doFilter(request, response);
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void sendErrorResponse(HttpServletResponse response, int statuscode, String msg)
            throws IOException {
        response.setStatus(statuscode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + msg + "\"}");
    }

    private String getAuthTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AuthId".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
