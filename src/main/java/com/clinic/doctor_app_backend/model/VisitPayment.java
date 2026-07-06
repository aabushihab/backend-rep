package com.clinic.doctor_app_backend.model;

import com.clinic.doctor_app_backend.choices.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "visit_payments")
@Data
public class VisitPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    @JsonBackReference
    private Visit visit;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // 🔑 STORE CASH PART ONLY
    @Column(nullable = true)
    private Double amount;
    // -------- INSURANCE --------
    private Double insuranceAmount;
    private String insuranceProvider;
    private String insuranceClass;
    private String insuranceType;
    private Integer coveragePercent;
    @Column(unique = true)  // Make insuranceAcceptNumber unique
    private String insuranceAcceptNumber;
    private String insuranceCardId;
    private String insuranceFormId;
    @Column(nullable = false)
    private Boolean insurancePaid = false;

    // -------- INSURANCE --------
    private Double insurancePaidAmount;  // Actual money received from insurance
    private Double insuranceDiscount;    // Approved discount/write-off

    // -------- POS --------
    private String terminalId;
    private String referenceNumber;
    private String cardType;
    private String approvalCode;

    private LocalDateTime paidAt = LocalDateTime.now();
//
//    @Transient
//    public Double getInsuranceOutstanding() {
//
//        double billed = insuranceAmount == null ? 0 : insuranceAmount;
//        double paid = insurancePaidAmount == null ? 0 : insurancePaidAmount;
//        double discount = insuranceDiscount == null ? 0 : insuranceDiscount;
//
//        return billed - paid - discount;
//    }
//
//    @Transient
//    public boolean isInsuranceSettled() {
//
//        double billed = insuranceAmount == null ? 0 : insuranceAmount;
//        double paid = insurancePaidAmount == null ? 0 : insurancePaidAmount;
//        double discount = insuranceDiscount == null ? 0 : insuranceDiscount;
//
//        return paid + discount >= billed;
//    }

}
