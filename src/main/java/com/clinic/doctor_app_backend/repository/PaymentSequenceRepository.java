package com.clinic.doctor_app_backend.repository;

import com.clinic.doctor_app_backend.model.PaymentSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentSequenceRepository extends JpaRepository<PaymentSequence, Long> {
    Optional<PaymentSequence> findByPaymentYearAndSequenceCode(Integer paymentYear, String sequenceCode);
}