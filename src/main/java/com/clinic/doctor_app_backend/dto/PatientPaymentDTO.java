package com.clinic.doctor_app_backend.dto;//package com.clinic.doctor_app_backend.dto;
//
//import com.clinic.doctor_app_backend.choices.PaymentMethod;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Data
//@AllArgsConstructor
//public class PatientPaymentDTO {
//    private Long paymentId;
//    private Double amount;
//    private PaymentMethod paymentMethod;
//    private LocalDateTime paidAt;
//    private String patientName;
//    private String doctorName;
//    private String visitType;
//    private LocalDateTime visitDate;
//    private String insuranceProvider;
//} above 22062026


import com.clinic.doctor_app_backend.choices.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PatientPaymentDTO {

    private Long paymentId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paidAt;

    private String patientName;
    private String doctorName;
    private String visitType;
    private LocalDateTime visitDate;

    // Insurance
    private String insuranceProvider;
    private Double insuranceAmount;
    private String insuranceClass;
    private String insuranceType;
    private Integer coveragePercent;
    private String insuranceAcceptNumber;
    private String insuranceCardId;
    private String insuranceFormId;
    private Boolean insurancePaid;
    private Double insurancePaidAmount;
    private Double insuranceDiscount;

    // POS
    private String terminalId;
    private String referenceNumber;
    private String cardType;
    private String approvalCode;
}
