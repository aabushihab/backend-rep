package com.clinic.doctor_app_backend.dto;

import lombok.Data;

@Data
public class DoctorVisitSummaryDto {

    private long totalVisits;
    private long totalClosed;
    private long totalOpen;
    private long totalToday;
    private long todayOpen;
}
