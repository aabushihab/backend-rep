package com.clinic.doctor_app_backend.dto;


import lombok.Data;

@Data
public class InsurancePaymentRequest {

    private String provider;
    private String cardNumber;
    private String policyNumber;
    private String approvalCode;
    private Integer coveragePercent;
}
