package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.service.VisitNativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient/history")
@RequiredArgsConstructor
@CrossOrigin("*")

public class VisitNativeController {

    private final VisitNativeService visitNativeService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Map<String, Object>>> getVisits(@PathVariable ("patientId") Long patientId) {

        List<Map<String, Object>> result =
                visitNativeService.getVisitsByPatientId(patientId);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }
}
