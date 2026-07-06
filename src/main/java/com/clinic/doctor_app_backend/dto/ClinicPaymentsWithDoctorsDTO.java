package com.clinic.doctor_app_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClinicPaymentsWithDoctorsDTO {
    private ClinicDurationPaymentDTO clinicTotals;       // total for the clinic
    private List<DoctorPaymentSummaryDTO> doctorSummaries; // per doctor breakdown
}
