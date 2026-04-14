package com.salesavvy.app.userServiceImplementaion;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.salesavvy.app.entites.User;
import com.salesavvy.app.userRepositories.UserRepo;
import com.salesavvy.app.userServices.UserServiceContract;

@Service
public class UserService implements UserServiceContract {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    // FIX: Inject the BCryptPasswordEncoder bean from SecurityConfig instead of
    // creating a new instance here. Creating `new BCryptPasswordEncoder()` bypasses
    // Spring's bean management and can cause inconsistency if SecurityConfig ever
    // changes the encoder strength/configuration.
    UserService(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        // isVerified defaults to false — user must verify via OTP after registering
        return userRepo.save(user);
    }
}
