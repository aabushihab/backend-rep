package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.choices.PaymentMethod;
import com.clinic.doctor_app_backend.choices.VisitStatus;
import com.clinic.doctor_app_backend.dto.*;
import com.clinic.doctor_app_backend.model.Drug;
import com.clinic.doctor_app_backend.model.Visit;
import com.clinic.doctor_app_backend.model.VisitDrug;
import com.clinic.doctor_app_backend.model.VisitPayment;
import com.clinic.doctor_app_backend.service.VisitService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin(origins = "*")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }
    @GetMapping("/by-date")
    public ResponseEntity<List<Visit>> getVisitsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        System.out.println("🔍 Searching for visits on date: " + date);

        List<Visit> visits = visitService.getVisitsByDate(date);

        System.out.println("📊 Found " + visits.size() + " visits for date: " + date);

        if (visits.isEmpty()) {
            System.out.println("⚠️ No visits found. Try: SELECT id, created_at FROM visit;");
        } else {
            visits.forEach(v -> {
                System.out.println("✅ Visit ID: " + v.getId() +
                        ", Created at: " + v.getCreatedAt());
            });
        }

        return ResponseEntity.ok(visits);
    }

    @GetMapping("/{visitId}/drug-prescriptions")
    public ResponseEntity<List<VisitDrugPrescriptionDto>>
    getDrugsPrescriptionByVisitId(
            @PathVariable("visitId") Long visitId) {

        return ResponseEntity.ok(
                visitService.getDrugsPrescriptionByVisitId(visitId)
        );
    }

    // ✅ Add drugs to existing visit
    @PostMapping("/{visitId}/drugs")
    public Visit addDrugsToVisit(
            @PathVariable("visitId") Long visitId,
            @RequestBody List<Long> drugIds) {

        return visitService.addDrugsToVisit(visitId, drugIds);
    }

    // ✅ Remove one drug from visit
    @DeleteMapping("/{visitId}/drugs/{drugId}")
    public Visit removeDrug(
            @PathVariable("visitId") Long visitId,
            @PathVariable("drugId") Long drugId
    ) {
        return visitService.removeDrug(visitId, drugId);
    }
//    // ✅ Get all drugs by visit ID
//    @GetMapping("/{visitId}/drugs")
//    public List<Drug> getDrugsByVisitId(            @PathVariable("visitId") Long visitId) {
//        return visitService.getDrugsByVisitId(visitId);
//    }
@GetMapping("/{visitId}/drugs")
public List<DrugDto> getDrugsByVisitId(
        @PathVariable("visitId") Long visitId) {

    return visitService.getDrugsByVisitId(visitId);
}
    @GetMapping("/{visitId}/payments")
    public VisitPaymentDetailsResponse getPaymentDetails(
                       @PathVariable("visitId") Long visitId) {

        return visitService.getPaymentDetails(visitId);
    }
    @PostMapping("/payments/{visitId}")
    public ResponseEntity<Void> payVisit(
            @PathVariable("visitId") Long visitId,
            @RequestBody VisitPayRequest req
    ) {
        visitService.payVisit(visitId, req);
        return ResponseEntity.ok().build();
    }



    @PutMapping("/{id}/details")
    public ResponseEntity<?> saveDetails(
            @PathVariable("id") Long id,
            @RequestBody VisitDetailsRequest request
    ) {
        visitService.updateVisitDetails(id, request);
        return ResponseEntity.ok().build();
    }


//    @PostMapping("/open")
//    public Visit openVisit(@RequestParam Long patientId,
//                           @RequestParam Long doctorId,
//                           @RequestParam(required = false) Long appointmentId) {
//        String visitType = (appointmentId != null) ? "appointment" : "walk_in";
//        return visitService.openVisit(patientId, doctorId, appointmentId, visitType);
//    }

//    @PutMapping("/{id}/status")
//    public Visit updateStatus(@PathVariable Long id, @RequestParam String status) {
//        return visitService.updateStatus(id, status);
//    }

    @PutMapping("/{id}/start")
    public Visit startVisitFromDoctor(@PathVariable("id") Long id) {
        return visitService.startVisitFromDoctor(id);
    }

    @PutMapping("/{id}/end")
    public Visit endVisitFromDoctor(@PathVariable("id") Long id) {
        return visitService.endVisitFromDoctor(id);
    }

    @PutMapping("/{id}/close")
    public Visit closeVisitFromDoctor(@PathVariable("id") Long id
    ) {
        return visitService.closeVisitFromDoctor(id);
    }

    // ------------------- UPDATE DOCTOR DETAILS -------------------
    @PutMapping("/{id}/updateDetails")
    public Visit updateVisitDetails(
            @PathVariable ("id") Long id,
            @RequestParam(required = false,name = "chiefComplaint") String chiefComplaint,
            @RequestParam(required = false,name = "history") String history,
            @RequestParam(required = false,name = "medications") String medications,
            @RequestParam(required = false,name = "allergies") String allergies,
            @RequestParam(required = false,name = "doctorNotes") String doctorNotes) {
        return visitService.updateVisitDetails(id, chiefComplaint, history, medications, allergies, doctorNotes);
    }

    @GetMapping("/{id}")
    public Visit getVisit(@PathVariable("id") Long id) {
        return visitService.getVisit(id).orElse(null);
    }

    @GetMapping("/patient/{patientId}")
    public List<Visit> getVisitsByPatient(@PathVariable("patientId") Long patientId) {
        return visitService.getVisitsByPatient(patientId);
    }

    @PostMapping("/patients")
    public Map<Long, List<Visit>> getVisitsByPatientIds(@RequestBody List<Long> patientIds) {
        return visitService.getVisitsByPatientIds(patientIds);
    }

    @GetMapping("/doctor/{doctorId}")
    public Page<Visit> getVisitsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return visitService.getVisitsByDoctor(
                doctorId,
                page,
                size
        );
    }

    @GetMapping("/doctor/{id}/summary")
    public DoctorVisitSummaryDto getDoctorSummary(@PathVariable Long id) {
        return visitService.getDoctorSummary(id);
    }

//    @GetMapping("/all")
//    public List<Visit> getAllVisits() {
//        return visitService.getAllVisits();
//    }

    @PutMapping("/{id}/reopen")
    public ResponseEntity<?> reopenVisit(@PathVariable("id") Long id) {
        visitService.reopenVisit(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{visitId}/drug-prescriptions")
    public ResponseEntity<VisitDrug> savePrescription(
            @PathVariable("visitId") Long visitId,
            @RequestBody VisitDrugDetailsRequest req
    ) {
        return ResponseEntity.ok(
                visitService.updateVisitDrugDetails(visitId, req)
        );
    }



    @PutMapping("/payments/insurance/{paymentId}/settle")
    public ResponseEntity<Void> settleInsurance(
            @PathVariable ("paymentId") Long paymentId,
            @RequestParam ("paidAmount") Double paidAmount,
            @RequestParam ("discountAmount") Double discountAmount
    ) {
        visitService.settleInsurance(paymentId, paidAmount, discountAmount);
        return ResponseEntity.ok().build();
    }


    // ================= CLAIMS API =================

    // All insurance payments
    @GetMapping
    public List<VisitPaymentFullDto> getAllInsurancePayments() {
        return visitService.getAllInsurancePayments()
                .stream()
                .map(this::toDto)
                .toList();
    }



    // Unpaid claims
    @GetMapping("/unpaid")
    public List<VisitPaymentFullDto> getUnpaidClaims() {
        return visitService.getUnpaidInsuranceClaims()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Outstanding claims
    @GetMapping("/outstanding")
    public List<VisitPaymentFullDto> getOutstandingClaims() {
        return visitService.getOutstandingInsuranceClaims()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Provider claims
    @GetMapping("/provider/{provider}")
    public List<VisitPaymentFullDto> getByProvider(@PathVariable String provider) {
        return visitService.getInsurancePaymentsByProvider(provider)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Claim follow-up by acceptance number
    @GetMapping("/accept-number/{acceptNumber}")
    public VisitPaymentFullDto getByAcceptNumber(@PathVariable String acceptNumber) {
        return toDto(
                visitService.getInsuranceClaimByAcceptNumber(acceptNumber)
        );
    }

    // Provider summary report (no DTO needed unless you want it)
    @GetMapping("/summary/providers")
    public List<Object[]> getProviderSummary() {
        return visitService.getInsuranceSummaryByProvider();
    }

    // Insurance payments between dates
    @GetMapping("/between-dates")
    public List<VisitPaymentFullDto> getInsurancePaymentsBetweenDates(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {
        return visitService.getInsurancePaymentsBetweenDates(fromDate, toDate)
                .stream()
                .map(this::toDto)
                .toList();
    }


    @GetMapping("/by-ids")
    public List<VisitPaymentFullDto> getByIds(@RequestParam List<Long> ids) {
        return visitService.getByIds(ids)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private VisitPaymentFullDto toDto(VisitPayment p) {

        VisitPaymentFullDto dto = new VisitPaymentFullDto();

        // ---------------- PAYMENT ----------------
        dto.id = p.getId();
        dto.paymentMethod = p.getPaymentMethod() != null ? p.getPaymentMethod().name() : null;


        // 🔥 ADD THESE LINES TO MAP PAYMENT NUMBER FIELDS
        dto.paymentNumber = p.getPaymentNumber();
        dto.paymentYear = p.getPaymentYear();
        dto.sequenceNumber = p.getSequenceNumber();
        // 🔥 END OF ADDED LINES

        dto.insuranceAmount = p.getInsuranceAmount();
        dto.insuranceProvider = p.getInsuranceProvider();
        dto.insuranceClass = p.getInsuranceClass();
        dto.insuranceType = p.getInsuranceType();
        dto.coveragePercent = p.getCoveragePercent();

        dto.insuranceAcceptNumber = p.getInsuranceAcceptNumber();
        dto.insurancePaid = p.getInsurancePaid();

        dto.insurancePaidAmount = p.getInsurancePaidAmount();
        dto.insuranceDiscount = p.getInsuranceDiscount();

        dto.paidAt = p.getPaidAt();

        // ---------------- VISIT ----------------
        if (p.getVisit() != null) {

            Visit v = p.getVisit();

            dto.visitId = v.getId();
            dto.currency = v.getCurrency();
            dto.originalAmount = v.getOriginalAmount();

            if (v.getPatient() != null) {
                dto.patientName = v.getPatient().getFirstName()+" "+v.getPatient().getLastName();
            }

            if (v.getDoctor() != null) {
                dto.doctorName = v.getDoctor().getFirstName();
            }

            dto.visitDate = String.valueOf(v.getVisitDate());
        }

        // ---------------- CALCULATIONS ----------------
        double billed = p.getInsuranceAmount() == null ? 0 : p.getInsuranceAmount();
        double paid = p.getInsurancePaidAmount() == null ? 0 : p.getInsurancePaidAmount();
        double discount = p.getInsuranceDiscount() == null ? 0 : p.getInsuranceDiscount();

        dto.outstanding = billed - paid - discount;
        dto.settled = (paid + discount) >= billed;

        return dto;
    }



    @GetMapping("/doctor/{doctorId}/search")
    public ResponseEntity<List<Visit>> searchVisits(
            @PathVariable Long doctorId,
            @RequestParam String name
    ) {
        List<Visit> result =
                visitService.searchVisitsByDoctorAndName(doctorId, name);

        return ResponseEntity.ok(result);
    }



    @GetMapping("/find/{id}")
    public ResponseEntity<Visit> getVisitById(@PathVariable ("id") Long id) {
        return ResponseEntity.ok(visitService.getVisitById(id));
    }
//
//    @GetMapping("/doctor/{doctorId}/visit/{visitId}/page")
//    public ResponseEntity<Integer> getVisitPage(
//            @PathVariable Long doctorId,
//            @PathVariable Long visitId,
//            @RequestParam(defaultValue = "40") int size
//    ) {
//        int page = visitService.findPageOfVisit(doctorId, visitId, size);
//        return ResponseEntity.ok(page);
//    }
}



