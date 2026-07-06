//package com.clinic.doctor_app_backend.service;
//
//import com.clinic.doctor_app_backend.repository.AppointmentRepository;
//import com.clinic.doctor_app_backend.repository.DoctorRepository;
//import com.clinic.doctor_app_backend.repository.PatientRepository;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class ReportService {
//
//    private final AppointmentRepository appointmentRepo;
//    private final PatientRepository patientRepo;
//    private final DoctorRepository doctorRepo;
//
//    public ReportService(AppointmentRepository appointmentRepo, PatientRepository patientRepo, DoctorRepository doctorRepo) {
//        this.appointmentRepo = appointmentRepo;
//        this.patientRepo = patientRepo;
//        this.doctorRepo = doctorRepo;
//    }
//
//    public List<Object[]> getAppointmentsLastMonth() {
//        LocalDateTime start = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
//        LocalDateTime end = LocalDate.now().withDayOfMonth(1).atStartOfDay();
//        return appointmentRepo.countAppointmentsLastMonth(start, end);
//    }
//
//    public List<Object[]> getAppointmentsPerDoctor() {
//        return appointmentRepo.countAppointmentsPerDoctor();
//    }
//
//    public List<Object[]> getPatientsByGender() {
//        return patientRepo.countByGender();
//    }
//
//    public List<Object[]> getPatientsByCity() {
//        return patientRepo.countByCity();
//    }
//
//    public List<Object[]> getDoctorsBySpecialty() {
//        return doctorRepo.countDoctorsBySpecialty();
//    }
//}


package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.model.Appointment;
import com.clinic.doctor_app_backend.model.WalkInModel;
import com.clinic.doctor_app_backend.repository.AppointmentRepository;
import com.clinic.doctor_app_backend.repository.DoctorRepository;
import com.clinic.doctor_app_backend.repository.PatientRepository;
import com.clinic.doctor_app_backend.repository.WalkInRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final AppointmentRepository appointmentRepo;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final WalkInRepository walkInRepo;

    public ReportService(AppointmentRepository appointmentRepo,
                         PatientRepository patientRepo,
                         DoctorRepository doctorRepo,
                         WalkInRepository walkInRepo) {
        this.appointmentRepo = appointmentRepo;
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.walkInRepo = walkInRepo;
    }

    // ---------------- APPOINTMENTS ----------------

    /**
     * Get detailed appointment report between two dates
     */
    public List<Appointment> getAppointmentsReport(LocalDateTime from, LocalDateTime to) {
        return appointmentRepo.findAll().stream()
                .filter(a -> a.getAppointmentTime() != null
                        && !a.getAppointmentTime().isBefore(from)
                        && !a.getAppointmentTime().isAfter(to))
                .toList();
    }

    /**
     * Get summary of appointments per doctor
     */
    public List<Object[]> getAppointmentsPerDoctor() {
        return appointmentRepo.countAppointmentsPerDoctor();
    }

    /**
     * Count appointments by status
     */
    public List<Object[]> getAppointmentsByStatus() {
        return appointmentRepo.countAppointmentsByStatus();
    }

    /**
     * Get appointments for a specific patient
     */
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepo.findByPatientId(patientId);
    }

    // ---------------- WALK-INS ----------------

    /**
     * Get all walk-ins for a specific date
     */
    public List<WalkInModel> getWalkInsByDate(LocalDate date) {
        return walkInRepo.findWalkInsByDate(date);
    }

    /**
     * Get walk-ins for a specific doctor and date
     */
    public List<WalkInModel> getWalkInsByDoctorAndDate(Long doctorId, LocalDate date) {
        return walkInRepo.findWalkInsByDate(date).stream()
                .filter(w -> w.getDoctor() != null && w.getDoctor().getId().equals(doctorId))
                .toList();
    }

    /**
     * Count total walk-ins per doctor
     */
    public List<Object[]> countWalkInsPerDoctor() {
        return walkInRepo.findAll().stream()
                .filter(w -> w.getDoctor() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        w -> w.getDoctor().getFullName(),
                        java.util.stream.Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .toList();
    }

    // ---------------- PATIENTS ----------------

    public List<Object[]> getPatientsByGender() {
        return patientRepo.countByGender();
    }

    public List<Object[]> getPatientsByCity() {
        return patientRepo.countByCity();
    }

    // ---------------- DOCTORS ----------------

    public List<Object[]> getDoctorsBySpecialty() {
        return doctorRepo.countDoctorsBySpecialty();
    }

}
