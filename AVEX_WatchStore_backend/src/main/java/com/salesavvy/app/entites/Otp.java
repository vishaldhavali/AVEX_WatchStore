package com.salesavvy.app.entites;

import jakarta.persistence.*;
import java.time.LocalDateTime;



@Entity
@Table(name = "otp_table")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oid;

    @Column(length = 10)
    private String otp;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Otp() {
    }

    public Otp(String otp, LocalDateTime createdAt, User user) {
        this.otp = otp;
        this.createdAt = createdAt;
        this.user = user;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}