package com.clinic.doctor_app_backend.choices;


public enum VisitStatus {

    CREATED,        // Visit created (walk-in registered OR Appointment Register)
    IN_PROGRESS,    // Doctor started consultation
    CLOSED,         // Doctor finished visit
    CANCELLED,       // Optional but strongly recommended
    COMPLETED
}
