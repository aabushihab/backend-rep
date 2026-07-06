package com.clinic.doctor_app_backend.dto;


import lombok.Data;
import java.util.List;

@Data
public class CreateFolderRequest {
    private String name;
    private String description;
}