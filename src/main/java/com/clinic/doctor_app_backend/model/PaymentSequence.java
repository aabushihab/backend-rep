package com.clinic.doctor_app_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payment_sequences")
@Data
public class PaymentSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_year", nullable = false)
    private Integer paymentYear;

    @Column(name = "last_sequence", nullable = false)
    private Long lastSequence;

    @Column(name = "sequence_code", nullable = false)
    private String sequenceCode;

    public PaymentSequence() {}

    public PaymentSequence(Integer paymentYear, Long lastSequence, String sequenceCode) {
        this.paymentYear = paymentYear;
        this.lastSequence = lastSequence;
        this.sequenceCode = sequenceCode;
    }
}