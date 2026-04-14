package com.salesavvy.app.Controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.salesavvy.app.Dto.OtpRequest;
import com.salesavvy.app.Dto.OtpVerifyRequest;
import com.salesavvy.app.userServices.OtpServiceContract;

/**
 * OTP endpoints — all public (no auth required).
 * Add "/api/users/send-otp" and "/api/users/verify-otp" to
 * UNAUTH_PATH in AuthenticationFilter and to permitAll() in SecurityConfig.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5167", allowCredentials = "true")
public class OtpController {

    private final OtpServiceContract otpService;

    public OtpController(OtpServiceContract otpService) {
        this.otpService = otpService;
    }

    /**
     * POST /api/users/send-otp
     * Body: { "email": "user@example.com" }
     *
     * Generates a 6-digit OTP and emails it to the registered address.
     * Use this for: account verification after registration, or password reset.
     */
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }
            otpService.sendOtp(request.getEmail());
            return ResponseEntity.ok(Map.of(
                    "message", "OTP sent successfully. Please check your email.",
                    "email", request.getEmail()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to send OTP. Please try again later."));
        }
    }

    /**
     * POST /api/users/verify-otp
     * Body: { "email": "user@example.com", "otp": "123456" }
     *
     * Verifies the OTP. On success, marks the user account as verified.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }
            if (request.getOtp() == null || request.getOtp().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "OTP is required"));
            }

            boolean verified = otpService.verifyOtp(request.getEmail(), request.getOtp());

            if (verified) {
                return ResponseEntity.ok(Map.of(
                        "message", "OTP verified successfully. Your account is now active.",
                        "verified", true
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "error", "Invalid OTP. Please check and try again.",
                                "verified", false
                        ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage(), "verified", false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "OTP verification failed. Please try again.", "verified", false));
        }
    }
}
