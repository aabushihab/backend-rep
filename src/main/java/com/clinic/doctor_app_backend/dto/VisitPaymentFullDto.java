package com.clinic.doctor_app_backend.dto;

import java.time.LocalDateTime;

public class VisitPaymentFullDto {

    public Long id;
    public Long visitId;

    public String paymentMethod;

    public Double amount;

    public Double insuranceAmount;
    public String insuranceProvider;
    public String insuranceClass;
    public String insuranceType;
    public Integer coveragePercent;

    public String insuranceAcceptNumber;
    public String insuranceCardId;
    public String insuranceFormId;

    public Boolean insurancePaid;
    public Double insurancePaidAmount;
    public Double insuranceDiscount;

    public String terminalId;
    public String referenceNumber;
    public String cardType;
    public String approvalCode;

    public LocalDateTime paidAt;

    // OPTIONAL computed fields
    public Double outstanding;
    public Boolean settled;

    // VISIT INFO (IMPORTANT for claims)
        public String patientName;
        public String doctorName;
        public String visitDate;
        public String currency;
        public Double originalAmount;



}
