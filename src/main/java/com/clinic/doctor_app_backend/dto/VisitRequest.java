package com.clinic.doctor_app_backend.dto;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VisitRequest {

    private Long patientId;
    private Long doctorId;
    private Long appointmentId;

    private String visitType;
    private LocalDateTime checkInTime;

    private List<Long> drugIds; // 🔥 important
}