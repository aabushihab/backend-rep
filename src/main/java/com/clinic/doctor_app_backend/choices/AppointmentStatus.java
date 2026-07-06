package com.clinic.doctor_app_backend.choices;

public enum AppointmentStatus {
    NEW,           // Appointment just created
    ATTENDED,      // Patient has checked in
    IN_PROGRESS,   // Appointment/visit started
    CLOSED   ,      // Appointment completed after visit
    CANCELLED      // Appointment was cancelled

    }

