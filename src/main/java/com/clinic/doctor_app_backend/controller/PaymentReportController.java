package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.dto.ClinicDurationPaymentDTO;
import com.clinic.doctor_app_backend.dto.ClinicPaymentSummaryDTO;
import com.clinic.doctor_app_backend.dto.ClinicPaymentsWithDoctorsDTO;
import com.clinic.doctor_app_backend.dto.DoctorPaymentSummaryDTO;
import com.clinic.doctor_app_backend.model.VisitPayment;
import com.clinic.doctor_app_backend.service.PaymentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports/payments")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PaymentReportController {

    private final PaymentReportService paymentReportService;


    // GET payments by acceptance number (case-insensitive)
    @GetMapping("/by-accept-number")
    public ResponseEntity<List<VisitPayment>> getByAcceptanceNumber(
            @RequestParam("acceptNumber") String acceptNumber
    ) {
        List<VisitPayment> payments = paymentReportService.getByAcceptanceNumber(acceptNumber);
        return ResponseEntity.ok(payments);
    }



    @PutMapping("/insurance/paid")
    public ResponseEntity<VisitPayment> markInsurancePaid(
            @RequestParam(required = false,name = "insuranceFormId") String insuranceFormId,
            @RequestParam(required = false,name = "insuranceCardId") String insuranceCardId
    ) {
        VisitPayment updatedPayment =
                paymentReportService.markInsuranceAsPaid(insuranceFormId, insuranceCardId);

        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/with-doctors")
    public ResponseEntity<?> getClinicPaymentsWithDoctors(
            @RequestParam ("fromDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam ("toDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {
        try {
            ClinicPaymentsWithDoctorsDTO dto = paymentReportService.getClinicPaymentsWithDoctors(fromDate, toDate);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }


    // ================= PER DOCTOR =================
    @GetMapping("/doctors")
    public List<DoctorPaymentSummaryDTO> doctorSummary(
            @RequestParam ("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam ("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {
        return paymentReportService.getDoctorSummary(from, to);
    }

    @GetMapping
    public ResponseEntity<?> getClinicPayments(
            @RequestParam ("fromDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {
        try {
            ClinicDurationPaymentDTO dto = paymentReportService.getClinicPayments(fromDate, toDate);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace(); // see full server exception
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

}
