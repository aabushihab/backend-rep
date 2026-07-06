package com.clinic.doctor_app_backend.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class UserActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;        // user who made the action
    private String action;          // example: "MARK_ATTENDED"
    private String details;         // example: "Appointment 5 moved to ATTENDED"

    private LocalDateTime timestamp = LocalDateTime.now();
}

