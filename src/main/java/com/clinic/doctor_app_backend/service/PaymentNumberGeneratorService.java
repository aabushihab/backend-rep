package com.clinic.doctor_app_backend.service;


import com.clinic.doctor_app_backend.model.PaymentSequence;
import com.clinic.doctor_app_backend.repository.PaymentSequenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentNumberGeneratorService {

    private final PaymentSequenceRepository sequenceRepository;

    public PaymentNumberGeneratorService(PaymentSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Transactional
    public synchronized String generatePaymentNumber() {
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        String sequenceCode = "PAY";

        PaymentSequence sequence = sequenceRepository
                .findByPaymentYearAndSequenceCode(currentYear, sequenceCode)
                .orElseGet(() -> {
                    PaymentSequence newSeq = new PaymentSequence(currentYear, 0L, sequenceCode);
                    return sequenceRepository.save(newSeq);
                });

        Long nextSequence = sequence.getLastSequence() + 1;
        sequence.setLastSequence(nextSequence);
        sequenceRepository.save(sequence);

        return String.format("%d%06d", currentYear, nextSequence);
    }

    @Transactional
    public synchronized String generatePaymentNumberWithPrefix(String prefix) {
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        String sequenceCode = prefix != null ? prefix : "PAY";

        PaymentSequence sequence = sequenceRepository
                .findByPaymentYearAndSequenceCode(currentYear, sequenceCode)
                .orElseGet(() -> {
                    PaymentSequence newSeq = new PaymentSequence(currentYear, 0L, sequenceCode);
                    return sequenceRepository.save(newSeq);
                });

        Long nextSequence = sequence.getLastSequence() + 1;
        sequence.setLastSequence(nextSequence);
        sequenceRepository.save(sequence);

        return String.format("%s-%d-%06d", sequenceCode, currentYear, nextSequence);
    }
}
