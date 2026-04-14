package com.salesavvy.app.userServices;

public interface OtpServiceContract {

    /**
     * Generates a 6-digit OTP, saves it to DB, and emails it to the user.
     * Throws IllegalArgumentException if no user is found with the given email.
     */
    void sendOtp(String email);

    /**
     * Verifies the OTP for the given email.
     * Returns true if the OTP is valid and not expired (within 5 minutes).
     * Returns false (or throws) if expired or incorrect.
     */
    boolean verifyOtp(String email, String otp);
}
