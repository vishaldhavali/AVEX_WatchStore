package com.salesavvy.app.userServiceImplementaion;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.salesavvy.app.entites.Otp;
import com.salesavvy.app.entites.User;
import com.salesavvy.app.userRepositories.OtpRepo;
import com.salesavvy.app.userRepositories.UserRepo;
import com.salesavvy.app.userServices.OtpServiceContract;

@Service
public class OtpService implements OtpServiceContract {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final OtpRepo otpRepo;
    private final UserRepo userRepo;
    private final JavaMailSender mailSender;

    public OtpService(OtpRepo otpRepo, UserRepo userRepo, JavaMailSender mailSender) {
        this.otpRepo = otpRepo;
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtp(String email) {
        // Validate user exists
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No account found with email: " + email));

        // Delete any previously stored OTPs for this user before generating a new one
        otpRepo.deleteAllByUserId(user.getUserId());

        // Generate a cryptographically secure 6-digit OTP
        String otpCode = String.format("%06d", RANDOM.nextInt(1_000_000));

        // Save OTP to DB
        Otp otpEntity = new Otp(otpCode, LocalDateTime.now(), user);
        otpRepo.save(otpEntity);

        // Send OTP via email
        sendOtpEmail(email, user.getUsername(), otpCode);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No account found with email: " + email));

        Optional<Otp> latestOtp = otpRepo.findTopByUserUserIdOrderByCreatedAtDesc(user.getUserId());

        if (latestOtp.isEmpty()) {
            throw new IllegalArgumentException("No OTP found for this account. Please request a new one.");
        }

        Otp otpEntity = latestOtp.get();

        // Check expiry (5 minutes)
        LocalDateTime expiryTime = otpEntity.getCreatedAt().plusMinutes(OTP_EXPIRY_MINUTES);
        if (LocalDateTime.now().isAfter(expiryTime)) {
            otpRepo.deleteAllByUserId(user.getUserId()); // Clean up expired OTP
            throw new IllegalArgumentException("OTP has expired. Please request a new one.");
        }

        // Check OTP value
        if (!otpEntity.getOtp().equals(otp)) {
            return false;
        }

        // OTP is valid — delete it so it cannot be reused
        otpRepo.deleteAllByUserId(user.getUserId());

        // Mark user as verified
        user.setVerified(true);
        userRepo.save(user);

        return true;
    }

    private void sendOtpEmail(String toEmail, String username, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your  Verification Code");
        message.setText(
                "Hi " + username + ",\n\n" +
                "Your OTP verification code is: " + otpCode + "\n\n" +
                "This code is valid for " + OTP_EXPIRY_MINUTES + " minutes.\n" +
                "Do not share this code with anyone.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "— Team "
        );
        mailSender.send(message);
    }
}
