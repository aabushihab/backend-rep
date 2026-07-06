package com.clinic.doctor_app_backend.dto;

import com.clinic.doctor_app_backend.choices.DischargeStatus;
import lombok.Data;

@Data
public class VisitDetailsRequest {
    private String chiefComplaint;
    private String history;
    private String medications;
    private String allergies;
    private String doctorNotes;
    private DischargeStatus dischargeStatus;

}
