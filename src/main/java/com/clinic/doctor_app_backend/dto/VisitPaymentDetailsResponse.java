package com.clinic.doctor_app_backend.dto;

import com.clinic.doctor_app_backend.choices.PaymentMethod;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VisitPaymentDetailsResponse {

    private Long visitId;

    // VISIT LEVEL
    private String currency;
    private Double originalAmount;

    private Double patientPaid;      // CASH + POS
    private Double insuranceAmount;   // INSURANCE COVERED
    private Double remainingAmount;
    private Double insurancePaidAmount;
    private Double insuranceDiscount;


    private boolean paid;

    private List<PaymentItem> payments;

    @Data
    public static class PaymentItem {

        private Long id;
        private PaymentMethod paymentMethod;

        // CASH / POS
        private Double amount;

        // INSURANCE
        private Double insuranceAmount;
        private Integer coveragePercent;
        private String insuranceProvider;
        private String insuranceClass;
        private String insuranceType;
        private String insuranceAcceptNumber;
        private Double insurancePaidAmount;
        private Double insuranceDiscount;

        // POS
        private String terminalId;
        private String referenceNumber;
        private String cardType;
        private String approvalCode;

        private LocalDateTime paidAt;
    }
}
