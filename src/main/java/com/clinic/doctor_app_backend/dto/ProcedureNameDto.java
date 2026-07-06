package com.clinic.doctor_app_backend.dto;

public class ProcedureNameDto {
    private String procedureName;

    public ProcedureNameDto(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getProcedureName() {
        return procedureName;
    }
}