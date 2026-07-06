package com.clinic.doctor_app_backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "visit_drugs")
@Data
public class VisitDrug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---------------- RELATIONS ----------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    @JsonBackReference("visit-drugs")
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

    private Drug drug;

//    // ---------------- OPTIONAL FIELDS (useful in real clinics) ----------------
//    private String dosage;
//    private String frequency;
//    private String duration;
//    private String instructions;

// ================= ALL SIMPLE TEXT =================

    private String duration;      // e.g. "5 days", "2 weeks"
    private String durationType;  // e.g. "Day", "Week", "Month"
    private String frequency;     // e.g. "1-0-1", "Twice daily"
    private String dose;          // e.g. "1 tablet"
    private String instructions;  // e.g. "After meals"
}