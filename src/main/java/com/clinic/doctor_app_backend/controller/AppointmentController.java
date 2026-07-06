package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.choices.AppointmentStatus;
import com.clinic.doctor_app_backend.model.Appointment;
import com.clinic.doctor_app_backend.model.Visit;
import com.clinic.doctor_app_backend.service.AppointmentService;
import com.clinic.doctor_app_backend.service.DoctorService;
import com.clinic.doctor_app_backend.service.LogService;
import com.clinic.doctor_app_backend.service.ScheduleConflictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin("*")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final ScheduleConflictService conflictService;
    private final LogService logService;

    public AppointmentController(AppointmentService appointmentService,
                                 DoctorService doctorService,
                                 ScheduleConflictService conflictService, LogService logService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.conflictService = conflictService;
        this.logService = logService;

    }

    // ------------------- CREATE APPOINTMENT -------------------
    @PostMapping("/patient/{patientId}/room/{roomId}/doctor/{doctorId}")
    public ResponseEntity<?> createAppointment(
            @PathVariable("patientId") Long patientId,
            @PathVariable("roomId") Long roomId,
            @PathVariable("doctorId") Long doctorId,
            @RequestBody Map<String, String> body) {

        try {
            LocalDateTime appointmentTime = LocalDateTime.parse(body.get("appointmentTime"));
            String notes = body.getOrDefault("notes", "");
            AppointmentStatus status = AppointmentStatus.valueOf(body.getOrDefault("status", "APPOINTMENT"));

            // ---------------- Cross-type conflict check ----------------
            // Ensure doctor or room is not already booked with an Appointment or WalkInModel
            conflictService.checkDoctorAndRoomAvailability(doctorId, roomId, appointmentTime, null, false);


            // ---------------- Create Appointment ----------------
            Appointment appt = appointmentService.createAppointment(patientId, doctorId, roomId, appointmentTime, notes, status);

            return ResponseEntity.ok(appt);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ------------------- GET APPOINTMENTS BY PATIENT -------------------
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable("patientId") Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    // ------------------- GET APPOINTMENTS BY DOCTOR -------------------
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable("doctorId") Long doctorId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ------------------- UPDATE APPOINTMENT (REASSIGN DOCTOR) -------------------
    @PutMapping("/{id}/reassign")
    public ResponseEntity<?> reassignDoctor(@PathVariable("id") Long id, @RequestBody Map<String, Long> body) {
        try {
            Long doctorId = body.get("doctorId");

            // ---------------- Cross-type conflict check ----------------
            conflictService.checkDoctorAndRoomAvailability(doctorId, null,
                    appointmentService.getAppointmentById(id).getAppointmentTime(), id, false);

            Appointment updated = appointmentService.reassignDoctor(id, doctorId);

            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ------------------- DELETE APPOINTMENT -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted successfully");
    }

    // ------------------- GET APPOINTMENTS BY DATE -------------------
    @GetMapping("/date/{date}")
    public List<Appointment> getAppointmentsByDate(@PathVariable("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return appointmentService.getAppointmentsByDate(localDate);
    }




    // --------------------- MARK AS ATTENDED ---------------------
    @PutMapping("/{id}/attend")
    public Appointment markAttended(
            @PathVariable("id") Long id,
            @RequestParam("username") String username
    ) {
        Appointment appt = appointmentService.markAttended(id, username);

        logService.log(
                username,
                "MARK_ATTENDED",
                "Appointment " + id + " marked as ATTENDED"
        );

        return appt;
    }


    // --------------------- START APPOINTMENT (IN_PROGRESS) ---------------------
//    @PutMapping("/{id}/start")
//    public Appointment startAppointment(
//            @PathVariable Long id,
//            @RequestParam String username
//    ) {
//        Appointment appt = appointmentService.startAppointment(id, username);
//
//        logService.log(
//                username,
//                "START_APPOINTMENT",
//                "Appointment " + id + " moved to IN_PROGRESS"
//        );
//
//        return appt;
//    }

    @PutMapping("/{id}/start")
    public Visit startAppointment(
            @PathVariable("id") Long id,
            @RequestParam("username") String username
    ) {
        return appointmentService.startAppointment(id, username);


    }
    // --------------------- CLOSE APPOINTMENT ---------------------
//    @PutMapping("/{id}/close")
//    public Appointment closeAppointment(
//            @PathVariable Long id,
//            @RequestParam String username
//    ) {
//        Appointment appt = appointmentService.closeAppointment(id, username);
//
//        logService.log(
//                username,
//                "CLOSE_APPOINTMENT",
//                "Appointment " + id + " closed"
//        );
//
//        return appt;
//    }

    @PutMapping("/{id}/close")
    public ResponseEntity<?> closeAppointment(
            @PathVariable("id") Long id,
            @RequestParam("username") String username
    ) {
        try {
            Appointment appt = appointmentService.closeAppointment(id, username);

            logService.log(username, "CLOSE_APPOINTMENT", "Appointment " + id + " closed");

            return ResponseEntity.ok(appt);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    // --------------------- CANCEL APPOINTMENT ---------------------
    @PutMapping("/{id}/cancel")
    public Appointment cancelAppointment(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("reason") String reason
    ) {
        return appointmentService.cancelAppointment(id, username, reason);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        boolean changed = doctorService.changePassword(username, oldPassword, newPassword);
        if (changed) return ResponseEntity.ok("Password changed successfully!");
        else return ResponseEntity.badRequest().body("Old password is incorrect!");
    }

}
