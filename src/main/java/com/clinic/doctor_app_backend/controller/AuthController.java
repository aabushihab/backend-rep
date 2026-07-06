package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.dto.LoginRequest;
import com.clinic.doctor_app_backend.dto.UserResponse;
import com.clinic.doctor_app_backend.model.User;
import com.clinic.doctor_app_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final UserService userService;


    // ---------------- RESET PASSWORD ----------------
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {

        userService.resetPassword(
                req.get("username"),
                req.get("newPassword")
        );

        return ResponseEntity.ok("Password reset successfully");
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new UserResponse(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    // ---------- ADD ASSISTANT ----------
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(
                    userService.createUser(request.getUsername(), request.getPassword())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // ---------- GET ALL USERS ----------
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            // Return all assistants including active status
            return ResponseEntity.ok(
                    userService.getAllUsers().stream()
                            .map(user -> Map.of(
                                    "username", user.getUsername(),
                                    "role", user.getRole(),
                                    "enabled", user.isActive()  // include active status
                            ))
                            .toList()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ---------- TOGGLE USER ACTIVE STATUS ----------
    @PutMapping("/users/{username}/toggle")
    public ResponseEntity<?> toggleUser(@PathVariable("username") String username) {
        try {
            User user = userService.toggleUserActive(username);
            return ResponseEntity.ok(Map.of(
                    "username", user.getUsername(),
                    "enabled", user.isActive()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
