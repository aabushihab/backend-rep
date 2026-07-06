package com.clinic.doctor_app_backend.dto;

import com.clinic.doctor_app_backend.choices.PaymentMethod;
import lombok.Data;

@Data
public class PaymentItem {

    private PaymentMethod paymentMethod;
    private Double amount;

    // POS
    private String terminalId;
    private String referenceNumber;
    private String cardType;
    private String approvalCode;

    // Insurance
    private String insuranceProvider;
    private Integer coveragePercent;
    private String insuranceApprovalCode;


    private Double insurancePaidAmount;
    private Double insuranceDiscount;
    private Double insuranceAmount;
}
