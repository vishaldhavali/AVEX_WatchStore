package com.salesavvy.app.userRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.salesavvy.app.entites.Otp;

@Repository
public interface OtpRepo extends JpaRepository<Otp, Integer> {

    // Fetch the latest OTP for a user (most recently created)
    Optional<Otp> findTopByUserUserIdOrderByCreatedAtDesc(int userId);

    // Delete all OTPs for a user (cleanup after verification or resend)
    @Modifying
    @Transactional
    @Query("DELETE FROM Otp o WHERE o.user.userId = :userId")
    void deleteAllByUserId(int userId);
}
