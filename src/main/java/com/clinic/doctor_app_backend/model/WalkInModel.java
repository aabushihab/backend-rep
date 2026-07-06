package com.clinic.doctor_app_backend.model;

import com.clinic.doctor_app_backend.choices.VisitStatus;
import com.clinic.doctor_app_backend.choices.VisitType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WalkInModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String doctorName;
    private LocalDateTime visitTime;
    private String notes;

    @JsonManagedReference
    @OneToOne(mappedBy = "walkIn")
    private Visit visit;

    @Enumerated(EnumType.STRING)
    private VisitType type;

    @Enumerated(EnumType.STRING)
    private VisitStatus status = VisitStatus.CREATED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Room room;

    // ---------------- AUDIT ----------------
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime inProgressAt;
    private LocalDateTime closedAt;
    private LocalDateTime cancelledAt;
    private String cancelReason;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = VisitStatus.CREATED;
        }
    }

    // ---------------- HELPER METHOD TO CREATE LINKED VISIT ----------------
    public Visit createLinkedVisit() {
        Visit newVisit = new Visit();
        newVisit.setWalkIn(this); // Set the bidirectional relationship
        newVisit.setPatient(this.patient);
        newVisit.setDoctor(this.doctor);
        newVisit.setVisitType(this.type != null ? this.type.toString() : "WALK_IN");
        newVisit.setVisitStatus(VisitStatus.CREATED);
        newVisit.setCheckInTime(LocalDateTime.now());
        newVisit.setCreatedAt(LocalDateTime.now());
        newVisit.setUpdatedAt(LocalDateTime.now());

        this.visit = newVisit; // Set the reference on this side
        return newVisit;
    }

    public void markInProgress(Doctor doctor, Room room) {
        this.status = VisitStatus.IN_PROGRESS;
        this.inProgressAt = LocalDateTime.now();

        if (doctor != null) {
            this.doctor = doctor;
            this.doctorName = doctor.getFirstName() + " " + doctor.getLastName();
        }

        if (room != null) {
            this.room = room;
        }

        // Update linked visit if exists
        if (this.visit != null) {
            this.visit.setVisitStatus(VisitStatus.IN_PROGRESS);
            this.visit.setUpdatedAt(LocalDateTime.now());
        }
    }

    public void markClosed() {
        this.status = VisitStatus.CLOSED;
        this.closedAt = LocalDateTime.now();

        // Update linked visit if exists
        if (this.visit != null) {
            this.visit.setVisitStatus(VisitStatus.CLOSED);
            this.visit.setUpdatedAt(LocalDateTime.now());
            this.visit.setConsultationEnd(LocalDateTime.now());
        }
    }

    public void markCancelled(String reason) {
        this.status = VisitStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.cancelReason = reason;

        // Update linked visit if exists
        if (this.visit != null) {
            this.visit.setVisitStatus(VisitStatus.CANCELLED);
            this.visit.setUpdatedAt(LocalDateTime.now());
        }
    }

    // Setter for visit that maintains bidirectional relationship
    public void setVisit(Visit visit) {
        this.visit = visit;
        if (visit != null && visit.getWalkIn() != this) {
            visit.setWalkIn(this);
        }
    }
}