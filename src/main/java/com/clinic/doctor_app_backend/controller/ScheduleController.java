package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.dto.PatientScheduleDTO;
import com.clinic.doctor_app_backend.model.Appointment;
import com.clinic.doctor_app_backend.model.WalkInModel;
import com.clinic.doctor_app_backend.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin("*")

public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/{patientId}")
    public PatientScheduleDTO getSchedule(@PathVariable ("patientId") Long patientId) {
        return scheduleService.getPatientSchedule(patientId);
    }


    @GetMapping
    public Map<String, Object> getScheduleForDate(
            @RequestParam(value = "date", required = false,name = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date == null) {
            date = LocalDate.now(); // default to today
        }

        List<Appointment> appointments = scheduleService.getAppointmentsForDate(date);
        List<WalkInModel> walkIns = scheduleService.getWalkInsForDate(date);

        Map<String, Object> response = new HashMap<>();
        response.put("appointments", appointments);
        response.put("walkIns", walkIns);

        return response;
    }

}
