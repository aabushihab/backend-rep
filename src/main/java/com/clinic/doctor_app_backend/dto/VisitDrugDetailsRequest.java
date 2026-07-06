package com.clinic.doctor_app_backend.dto;

import lombok.Data;


@Data
public class VisitDrugDetailsRequest {
    private Long drugId;

    private String duration;
    private Integer durationType; // 1 Hour, 2 Day, 3 Week, 4 Month, 5 Year

    private String frequency;     // STRING (as you requested)
    private String dose;
    private String instruction;
}