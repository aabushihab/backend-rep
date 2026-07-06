package com.clinic.doctor_app_backend.dto;

import com.clinic.doctor_app_backend.choices.PaymentMethod;
import lombok.Data;

//package com.clinic.doctor_app_backend.dto;
//
//
//import lombok.Data;
//
//@Data
//public class VisitPaymentRequest {
//
//    private double originalAmount;
//    private double cashAmount;
//    private Double insuranceAmount;   // nullable
//
//    // insurance only
//    private String provider;
//    private String insuranceClass;
//    private Integer coveragePercent;
//    private String insuranceType; // BENEFICIARY or SUBSCRIBER
//    private String cardId;              // OR
//    private String insuranceFormId;      // OR
//
//    private String insuranceAcceptNumber; // REQUIRED
//}
//
@Data
public class VisitPaymentRequest {

    private PaymentMethod paymentMethod;

    private Double originalAmount;
    private Double cashAmount;
    private Double insuranceAmount;



    // -------- INSURANCE --------
    private String insuranceProvider;
    private String insuranceClass;   // A / B / C
    private String insuranceType;    // BENEFICIARY / SUBSCRIBER
    private Integer coveragePercent;
    private String insuranceAcceptNumber;
    private String cardId;
    private String insuranceFormId;

    // -------- POS --------
    private String posPaymentNo;
    private String terminalId;
    private String cardType;
    private String approvalCode;
}
