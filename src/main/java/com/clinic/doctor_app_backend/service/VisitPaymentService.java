//package com.clinic.doctor_app_backend.service;
//
//
//import com.clinic.doctor_app_backend.choices.PaymentMethod;
//import com.clinic.doctor_app_backend.choices.VisitStatus;
//import com.clinic.doctor_app_backend.dto.InsurancePaymentRequest;
//import com.clinic.doctor_app_backend.model.Visit;
//import com.clinic.doctor_app_backend.repository.VisitRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class VisitPaymentService {
//
//    private final VisitRepository visitRepository;
//
//    // ---------------- PAY VISIT ----------------
//    @Transactional
//    public Visit payVisit(Long visitId,
//                          PaymentMethod paymentMethod,
//                          InsurancePaymentRequest insuranceRequest) {
//
//        Visit visit = visitRepository.findById(visitId)
//                .orElseThrow(() -> new RuntimeException("Visit not found"));
//
//        if (visit.isPaid()) {
//            throw new IllegalStateException("Visit already paid");
//        }
//
//        visit.setPaid(true);
//        visit.setPaymentMethod(paymentMethod);
//
//        // Insurance payment
//        if (paymentMethod == PaymentMethod.INSURANCE && insuranceRequest != null) {
//            visit.setInsuranceProvider(insuranceRequest.getProvider());
//            visit.setInsuranceCardNumber(insuranceRequest.getCardNumber());
//            visit.setInsurancePolicyNumber(insuranceRequest.getPolicyNumber());
//            visit.setInsuranceApprovalCode(insuranceRequest.getApprovalCode());
//            visit.setInsuranceCoveragePercent(insuranceRequest.getCoveragePercent());
//        }
//
//        // Optional: move visit to COMPLETED after payment
//        visit.setVisitStatus(VisitStatus.COMPLETED);
//
//        return visitRepository.save(visit);
//    }
//
//    // ---------------- UNPAID VISITS ----------------
//    public List<Visit> getUnpaidVisits() {
//        return visitRepository.findByPaidFalse();
//    }
//
//    // ---------------- PAID VISITS ----------------
//    public List<Visit> getPaidVisits() {
//        return visitRepository.findByPaidTrue();
//    }
//
//    // ---------------- UNPAID VISITS BY DOCTOR ----------------
//    public List<Visit> getUnpaidVisitsByDoctor(Long doctorId) {
//        return visitRepository.findByDoctorIdAndPaidFalse(doctorId);
//    }
//
//    // ---------------- INSURANCE VISITS ----------------
//    public List<Visit> getInsuranceVisits() {
//        return visitRepository.findByInsuranceProviderIsNotNull();
//    }
//}
//
