package com.clinic.doctor_app_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClinicDurationPaymentDTO {


//    private Double insuranceDiscount;
//
//    private Double cashTotal;
//    private Double posTotal;
//    private Double insuranceTotal;
//
//    private Long freeVisits;
//
//    private Double grandTotal;

    private Double cashTotal;
    private Double posTotal;
    private Double insuranceTotal;
    private Double insuranceDiscount;
    private Long freeVisits;
    private Double grandTotal;
}
