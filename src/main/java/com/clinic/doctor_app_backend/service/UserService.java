package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.model.User;
import com.clinic.doctor_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // LOGIN
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User is disabled");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
    // ---------------- RESET PASSWORD ----------------
    public void resetPassword(String username, String newPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    // CREATE USER (ROLE FORCED)
    public User createUser(String username, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ASSISTANT"); // 🔒 FORCED
        user.setActive(true);
        return userRepository.save(user);
    }

// inside UserService.java

    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> "ASSISTANT".equals(user.getRole())) // no .name()
                .toList();
    }

    public User toggleUserActive(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(!user.isActive()); // toggle active status
        return userRepository.save(user);
    }




}
