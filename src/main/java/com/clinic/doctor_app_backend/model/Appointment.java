package com.clinic.doctor_app_backend.model;

import com.clinic.doctor_app_backend.choices.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String doctorName;

    private LocalDateTime appointmentTime;

    private String notes;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.NEW; // default NEW

    // ---------------- NEW DATE FIELDS ----------------

    // Appointment Creation Date
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    // Appointment Attend Date
    private LocalDateTime attendDate;

    // Appointment In Progress Date
    private LocalDateTime inProgressDate;

    // Appointment Closed Date
    private LocalDateTime closedDate;

    // Appointment Cancel Date
    private LocalDateTime cancelDate;

    // Cancel Reason
    private String cancelReason;

    // Link to patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    // Link to doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Room room;

    // ---------------- LIFECYCLE CALLBACK ----------------

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }

    // ---------------- HELPER METHODS ----------------

    public void markAttended() {
        this.status = AppointmentStatus.ATTENDED;
        this.attendDate = LocalDateTime.now();
    }

    public void markInProgress() {
        this.status = AppointmentStatus.IN_PROGRESS;
        this.inProgressDate = LocalDateTime.now();
    }

    public void markClosed() {
        this.status = AppointmentStatus.CLOSED;
        this.closedDate = LocalDateTime.now();
    }

    public void markCancelled(String reason) {
        this.status = AppointmentStatus.CANCELLED;
        this.cancelDate = LocalDateTime.now();
        this.cancelReason = reason;
    }
}
