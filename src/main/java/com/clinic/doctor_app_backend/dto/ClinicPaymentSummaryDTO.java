package com.clinic.doctor_app_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClinicPaymentSummaryDTO {

    private Double cashTotal;
    private Double posTotal;
    private Double insuranceTotal;
    private Long freeVisits;

    private Double grandTotal;
}
