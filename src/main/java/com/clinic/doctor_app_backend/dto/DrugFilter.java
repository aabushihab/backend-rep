package com.clinic.doctor_app_backend.dto;


import lombok.Data;

@Data
public class DrugFilter {

    private String tradeName;
    private String genericName;
    private String atcCode;
    private String ingredients;
    private String dosageForm;
}