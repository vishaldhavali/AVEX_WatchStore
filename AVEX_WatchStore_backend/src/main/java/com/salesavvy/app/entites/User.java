package com.salesavvy.app.entites;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // FIX: Added isVerified to support OTP-based email verification.
    // New accounts are unverified until the user confirms their OTP.
    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at = LocalDateTime.now();

    public User() {
    }

    public User(String username, String email, String password, Role role, LocalDateTime created_at,
            LocalDateTime updated_at) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public User(String username, String email, String password, Role role) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(int userId, String username, String email, String password, Role role, LocalDateTime created_at,
            LocalDateTime updated_at) {
        super();
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int hashCode() {
        return Objects.hash(created_at, email, password, role, updated_at, userId, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(created_at, other.created_at) && Objects.equals(email, other.email)
                && Objects.equals(password, other.password) && role == other.role
                && Objects.equals(updated_at, other.updated_at) && userId == other.userId
                && Objects.equals(username, other.username);
    }

    @Override
    public String toString() {
        return "Users [userId=" + userId + ", username=" + username + ", email=" + email + ", role=" + role
                + ", isVerified=" + isVerified + ", created_at=" + created_at + ", updated_at=" + updated_at + "]";
    }
}
