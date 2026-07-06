package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.choices.PaymentMethod;
import com.clinic.doctor_app_backend.dto.PatientPaymentDTO;
import com.clinic.doctor_app_backend.model.Patient;
import com.clinic.doctor_app_backend.model.VisitPayment;
import com.clinic.doctor_app_backend.service.PatientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin("*")

public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/payment-method/{paymentMethod}")
    public ResponseEntity<List<PatientPaymentDTO>> getPatientsByPaymentMethod(
            @PathVariable("paymentMethod") PaymentMethod paymentMethod,
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {
        List<PatientPaymentDTO> patients = patientService.getPaymentsByMethodAndDateRange(paymentMethod, fromDate, toDate);
        return ResponseEntity.ok(patients);
    }

    // ---------------- CREATE ----------------

    /** Create TEMP patient */
    @PostMapping("/temp")
    public ResponseEntity<Patient> createTempPatient(@RequestBody Patient patient) {
        Patient created = patientService.createTempPatient(patient);
        return ResponseEntity.ok(created);
    }

    /** Create PERMANENT patient */
    @PostMapping("/permanent")
    public ResponseEntity<Patient> createPermanentPatient(@RequestBody Patient patient) {
        Patient created = patientService.createPermanentPatient(patient);
        return ResponseEntity.ok(created);
    }

    /** Upgrade TEMP → PERMANENT */
    @PutMapping("/upgrade/{id}")
    public ResponseEntity<Patient> upgradeToPermanent(@PathVariable("id") Long id, @RequestBody Patient fullPatient) {
        Patient upgraded = patientService.upgradeToPermanent(id, fullPatient);
        return ResponseEntity.ok(upgraded);
    }

    // ---------------- SEARCH ----------------

    @GetMapping("/search/mobile/{mobile}")
    public ResponseEntity<List<Patient>> searchByMobile(@PathVariable("mobile") String mobile) {
        System.out.println("Controller received: [" + mobile + "]");
        return ResponseEntity.ok(patientService.searchByMobile(mobile));
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<Patient>> searchByFirstOrLastName(@PathVariable("name") String name) {
        return ResponseEntity.ok(patientService.searchByFirstOrLastName(name));
    }

    @GetMapping("/search/id/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // ---------------- UPDATE ----------------

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
        Patient updated = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updated);
    }

    // ---------------- DELETE ----------------

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }



}
