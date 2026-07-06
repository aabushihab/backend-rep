package com.clinic.doctor_app_backend.dto;

import com.clinic.doctor_app_backend.choices.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class VisitPayRequest {

    private Double originalAmount;

    private List<PaymentPart> payments;
    // ADD THIS
    private String currency;
    @Data
    public static class PaymentPart {
        private PaymentMethod paymentMethod;
        private Double cashAmount;

        // POS fields
        private String posPaymentNo;
        private String terminalId;
        private String cardType;
        private String approvalCode;
        private String posAmount;
        // Insurance fields
        private Double insuranceAmount;
        private String insuranceProvider;
        private String insuranceClass;
        private String insuranceType;
        private Integer coveragePercent;
        private String insuranceAcceptNumber;
        private String cardId;            // optional
        private String insuranceFormId;   // optional
    }
}
