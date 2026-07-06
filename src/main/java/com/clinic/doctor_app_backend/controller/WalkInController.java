package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.choices.VisitType;
import com.clinic.doctor_app_backend.model.Appointment;
import com.clinic.doctor_app_backend.model.Visit;
import com.clinic.doctor_app_backend.model.WalkInModel;
import com.clinic.doctor_app_backend.service.WalkINService;
import com.clinic.doctor_app_backend.service.ScheduleConflictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient/visits")
@CrossOrigin("*")

public class WalkInController {

    private final WalkINService visitService;
    private final ScheduleConflictService conflictService;

    public WalkInController(WalkINService visitService, ScheduleConflictService conflictService) {
        this.visitService = visitService;
        this.conflictService = conflictService;
    }

    // ---------------- CREATE VISIT ----------------
    @PostMapping("/patient/{patientId}/room/{roomId}/doctor/{doctorId}")
    public ResponseEntity<?> createVisit(
            @PathVariable ("patientId") Long patientId,
            @PathVariable ("roomId") Long roomId,
            @PathVariable ("doctorId") Long doctorId,
            @RequestBody Map<String, String> body) {

        try {
            // Parse visit time
            LocalDateTime visitTime = LocalDateTime.parse(body.get("visitTime"));

            String notes = body.getOrDefault("notes", "");

            // Safely parse visit type
            String typeStr = body.get("type");
            VisitType type;
            if (typeStr == null || typeStr.isBlank()) {
                type = VisitType.WALK_IN;
            } else {
                type = VisitType.valueOf(typeStr);
            }

            // Conflict check
            conflictService.checkDoctorAndRoomAvailability(doctorId, roomId, visitTime, null, true);

            // Create visit
            WalkInModel visit = visitService.createVisit(
                    patientId, doctorId, roomId, visitTime, notes, type
            );

            return ResponseEntity.ok(visit);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ---------------- GET VISITS BY PATIENT ----------------
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<WalkInModel>> getVisitsByPatient(@PathVariable ("patientId") Long patientId) {
        return ResponseEntity.ok(visitService.getVisitsByPatient(patientId));
    }

    // ---------------- UPDATE VISIT ----------------
    @PutMapping("/{visitId}")
    public ResponseEntity<?> updateVisit(
            @PathVariable ("visitId") Long visitId,
            @RequestBody Map<String, String> body) {

        try {
            Long doctorId = Long.parseLong(body.get("doctorId"));
            Long roomId = Long.parseLong(body.get("roomId"));
            LocalDateTime visitTime = LocalDateTime.parse(body.get("visitTime"));
            String notes = body.getOrDefault("notes", "");

            String typeStr = body.get("type");
            VisitType type;
            if (typeStr == null || typeStr.isBlank()) {
                type = VisitType.WALK_IN; // default
            } else {
                type = VisitType.valueOf(typeStr);
            }

            // Conflict check
            conflictService.checkDoctorAndRoomAvailability(doctorId, roomId, visitTime, visitId, true);

            // Update visit
            WalkInModel visit = visitService.updateVisit(
                    visitId, doctorId, roomId, visitTime, notes, type
            );

            return ResponseEntity.ok(visit);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

//    // ---------------- DELETE VISIT ----------------
//    @DeleteMapping("/{visitId}")
//    public ResponseEntity<String> deleteVisit(@PathVariable Long visitId) {
//        visitService.deleteVisit(visitId);
//        return ResponseEntity.ok("Visit deleted successfully");
//    }

    // ---------------- GET VISITS BY DOCTOR ----------------
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<WalkInModel>> getVisitsByDoctor(@PathVariable ("doctorId") Long doctorId) {
        return ResponseEntity.ok(visitService.getVisitsByDoctor(doctorId));
    }
    @PutMapping("/{id}/close")
    public WalkInModel closeVisit(@PathVariable ("id") Long id) {
        return visitService.closeVisit(id);
    }
    // --------------------- CANCEL VISIT ---------------------
    @PutMapping("/{id}/cancel")
    public WalkInModel cancelVisit(
            @PathVariable ("id") Long id,
            @RequestParam  ("username") String username,
            @RequestParam ("reason") String reason
    ) {
        return visitService.cancelVisit(id, username, reason);
    }

    // --------------------- START VISIT ---------------------
    @PutMapping("/{visitId}/start")
    public ResponseEntity<?> startVisit(
            @PathVariable ("id") Long visitId,
            @RequestParam(name = "walkinId", required = false) Long walkinId,
            @RequestParam(name = "walkInId", required = false) Long walkinIdAlt,
            @RequestParam(required = false,name = "username") String username
    ) {
        if (username == null || username.isBlank()) {
            username = "unknown";
        }

        if (walkinId == null) {
            walkinId = walkinIdAlt;
        }

        WalkInModel visit = visitService.startVisit(visitId, username, walkinId);
        return ResponseEntity.ok(visit);
    }


    @PostMapping("/{walkInId}/create-visit")
    public ResponseEntity<Visit> createVisitFromWalkIn(
            @PathVariable ("walkInId") Long walkInId
    ) {
        Visit visit = visitService.createVisitFromWalkIn(walkInId);
        return ResponseEntity.ok(visit);
    }

}
