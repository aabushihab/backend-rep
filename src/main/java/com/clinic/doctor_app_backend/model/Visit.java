package com.clinic.doctor_app_backend.model;

import com.clinic.doctor_app_backend.choices.DischargeStatus;
import com.clinic.doctor_app_backend.choices.PaymentMethod;
import com.clinic.doctor_app_backend.choices.VisitStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "visits")
@Data
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ---------------- RELATIONSHIPS ----------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "patient", "doctor"})
    private Appointment appointment;

    // ---------------- VISIT DETAILS ----------------
    @Column(name = "visit_type", length = 50, nullable = false)
    private String visitType;

    @Enumerated(EnumType.STRING)
    private DischargeStatus dischargeStatus = DischargeStatus.NOT_DISCHARGED; // default


    @Enumerated(EnumType.STRING)
    private VisitStatus visitStatus;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    private LocalDateTime triageTime;
    private LocalDateTime consultationStart;
    private LocalDateTime consultationEnd;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String chiefComplaint;

    @Column(columnDefinition = "TEXT")
    private String history;

    @Column(columnDefinition = "TEXT")
    private String medications;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String doctorNotes;

    // ---------------- AUDIT ----------------
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime paiedAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    // In Visit
    @OneToOne
    @JoinColumn(name = "walkin_id")
    @JsonBackReference
    private WalkInModel walkIn;



    // ---------------- PAYMENT ----------------
//    @Column(nullable = false)
//    private boolean paid = false;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "payment_method")
//    private PaymentMethod paymentMethod;
//
//    @Column(name = "amount", nullable = false)
//    private Double originalAmount = 0.0;
//
//    private double cashAmount;
//    private double insuranceAmount;
//


//    // ---------------- INSURANCE PAYMENT DETAILS ----------------
//    @Column(name = "insurance_provider")
//    private String insuranceProvider;
//
//    @Column(name = "insurance_card_number")
//    private String insuranceCardNumber;
//
//    @Column(name = "insurance_policy_number")
//    private String insurancePolicyNumber;
//
//    @Column(name = "insurance_approval_code")
//    private String insuranceApprovalCode;
//
//    @Column(name = "insurance_coverage_percent")
//    private Integer insuranceCoveragePercent;
//
//    @Column(name = "insurance_type")
//    private String insuranceType; // BENEFICIARY or SUBSCRIBER


    // ---------------- PAYMENTS ----------------
    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<VisitPayment> payments = new ArrayList<>();


    private Double originalAmount;
    private boolean paid;
    @Column(length = 10)
    private String currency;

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @JsonProperty("visitDate")
    public LocalDateTime getVisitDate() {
        if (appointment != null && appointment.getAppointmentTime() != null) {
            return appointment.getAppointmentTime();
        }
        if (walkIn != null && walkIn.getVisitTime() != null) {
            return walkIn.getVisitTime();
        }
        return checkInTime; // safe fallback
    }

//
//    @ManyToMany
//    @JoinTable(
//            name = "visit_drugs",
//            joinColumns = @JoinColumn(name = "visit_id"),
//            inverseJoinColumns = @JoinColumn(name = "drug_id")
//    )
//    private List<Drug> drugs = new ArrayList<>();
@OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("visit-drugs")
private List<VisitDrug> visitDrugs = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "visit_procedures", joinColumns = @JoinColumn(name = "visit_id"))
    @Column(name = "procedure")
    private List<String> procedures = new ArrayList<>();


}
