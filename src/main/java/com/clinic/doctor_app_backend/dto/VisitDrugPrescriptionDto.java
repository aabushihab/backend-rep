package com.clinic.doctor_app_backend.dto;

import lombok.Data;

@Data
public class VisitDrugPrescriptionDto {

    private Long drugId;
    private String tradeName;
    private String strength;
    private String packageSize;
    private String dosageUnit;

    private String duration;
    private String durationType;
    private String frequency;
    private String dose;
    private String instructions;
}