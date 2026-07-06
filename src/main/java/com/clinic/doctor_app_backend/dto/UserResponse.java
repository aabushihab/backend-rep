package com.clinic.doctor_app_backend.dto;

import com.clinic.doctor_app_backend.model.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String username;
    private final String role;
    private final boolean enabled; // <-- new field

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole(); // keep actual role
        this.enabled = user.isActive(); // map DB field
    }
}
