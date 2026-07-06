package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.model.Appointment;
import com.clinic.doctor_app_backend.model.WalkInModel;
import com.clinic.doctor_app_backend.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
public class ReportController {


    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ---------------- APPOINTMENTS ----------------

    /** Get detailed appointment report between two dates */
    @GetMapping("/appointments")
    public List<Appointment> getAppointmentsReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return reportService.getAppointmentsReport(from, to);
    }

    /** Get summary of appointments per doctor */
    @GetMapping("/appointments/per-doctor")
    public List<Object[]> getAppointmentsPerDoctor() {
        return reportService.getAppointmentsPerDoctor();
    }

    /** Get appointment counts by status */
    @GetMapping("/appointments/by-status")
    public List<Object[]> getAppointmentsByStatus() {
        return reportService.getAppointmentsByStatus();
    }

    /** Get appointments for a specific patient */
    @GetMapping("/appointments/patient/{patientId}")
    public List<Appointment> getAppointmentsByPatient(@PathVariable ("patientId")Long patientId) {
        return reportService.getAppointmentsByPatient(patientId);
    }

    // ---------------- WALK-INS ----------------

    /** Get all walk-ins for a specific date */
    @GetMapping("/walkins")
    public List<WalkInModel> getWalkInsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return reportService.getWalkInsByDate(date);
    }

    /** Get walk-ins for a specific doctor and date */
    @GetMapping("/walkins/doctor/{doctorId}")
    public List<WalkInModel> getWalkInsByDoctorAndDate(
            @PathVariable ("doctorId") Long doctorId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return reportService.getWalkInsByDoctorAndDate(doctorId, date);
    }

    /** Count total walk-ins per doctor */
    @GetMapping("/walkins/per-doctor")
    public List<Object[]> countWalkInsPerDoctor() {
        return reportService.countWalkInsPerDoctor();
    }

    // ---------------- PATIENTS ----------------

    /** Count patients by gender */
    @GetMapping("/patients/by-gender")
    public List<Object[]> getPatientsByGender() {
        return reportService.getPatientsByGender();
    }

    /** Count patients by city */
    @GetMapping("/patients/by-city")
    public List<Object[]> getPatientsByCity() {
        return reportService.getPatientsByCity();
    }

    // ---------------- DOCTORS ----------------

    /** Count doctors by specialty */
    @GetMapping("/doctors/by-specialty")
    public List<Object[]> getDoctorsBySpecialty() {
        return reportService.getDoctorsBySpecialty();
    }
}
