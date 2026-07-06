package com.clinic.doctor_app_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}
