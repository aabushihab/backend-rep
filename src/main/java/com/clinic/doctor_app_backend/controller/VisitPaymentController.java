//package com.clinic.doctor_app_backend.controller;
//
//
//import com.clinic.doctor_app_backend.choices.PaymentMethod;
//import com.clinic.doctor_app_backend.dto.InsurancePaymentRequest;
//import com.clinic.doctor_app_backend.model.Visit;
//import com.clinic.doctor_app_backend.service.VisitPaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/visits/payments")
//@RequiredArgsConstructor
//public class VisitPaymentController {
//
//    private final VisitPaymentService visitPaymentService;
//
//    // ---------------- PAY VISIT ----------------
//    @PostMapping("/{visitId}/pay")
//    public Visit payVisit(@PathVariable Long visitId,
//                          @RequestParam PaymentMethod paymentMethod,
//                          @RequestBody(required = false) InsurancePaymentRequest insuranceRequest) {
//
//        return visitPaymentService.payVisit(
//                visitId,
//                paymentMethod,
//                insuranceRequest
//        );
//    }
//
//    // ---------------- UNPAID VISITS ----------------
//    @GetMapping("/unpaid")
//    public List<Visit> getUnpaidVisits() {
//        return visitPaymentService.getUnpaidVisits();
//    }
//
//    // ---------------- PAID VISITS ----------------
//    @GetMapping("/paid")
//    public List<Visit> getPaidVisits() {
//        return visitPaymentService.getPaidVisits();
//    }
//
//    // ---------------- UNPAID VISITS BY DOCTOR ----------------
//    @GetMapping("/unpaid/doctor/{doctorId}")
//    public List<Visit> getUnpaidVisitsByDoctor(@PathVariable Long doctorId) {
//        return visitPaymentService.getUnpaidVisitsByDoctor(doctorId);
//    }
//
//    // ---------------- INSURANCE VISITS ----------------
//    @GetMapping("/insurance")
//    public List<Visit> getInsuranceVisits() {
//        return visitPaymentService.getInsuranceVisits();
//    }
//}
