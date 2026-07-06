package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.choices.PaymentMethod;
import com.clinic.doctor_app_backend.dto.ClinicDurationPaymentDTO;
import com.clinic.doctor_app_backend.dto.ClinicPaymentSummaryDTO;
import com.clinic.doctor_app_backend.dto.ClinicPaymentsWithDoctorsDTO;
import com.clinic.doctor_app_backend.dto.DoctorPaymentSummaryDTO;
import com.clinic.doctor_app_backend.model.VisitPayment;
import com.clinic.doctor_app_backend.repository.VisitPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentReportService {

    private final VisitPaymentRepository visitPaymentRepository;



    public VisitPayment markInsuranceAsPaid(String insuranceFormId, String insuranceCardId) {

        VisitPayment payment;

        if (insuranceFormId != null && !insuranceFormId.isBlank()) {
            payment = visitPaymentRepository
                    .findByInsuranceFormId(insuranceFormId)
                    .orElseThrow(() ->
                            new RuntimeException("Insurance payment not found for formId: " + insuranceFormId)
                    );

        } else if (insuranceCardId != null && !insuranceCardId.isBlank()) {
            payment = visitPaymentRepository
                    .findByInsuranceCardId(insuranceCardId)
                    .orElseThrow(() ->
                            new RuntimeException("Insurance payment not found for cardId: " + insuranceCardId)
                    );

        } else {
            throw new RuntimeException("Insurance Form ID or Card ID is required");
        }

        payment.setInsurancePaid(true);
        payment.setPaidAt(LocalDateTime.now());

        return visitPaymentRepository.save(payment);
    }



    public ClinicPaymentsWithDoctorsDTO getClinicPaymentsWithDoctors(LocalDateTime from, LocalDateTime to) {
        ClinicDurationPaymentDTO clinicTotals = getClinicPayments(from, to);   // already implemented
        List<DoctorPaymentSummaryDTO> doctorSummaries = getDoctorSummary(from, to);
        return new ClinicPaymentsWithDoctorsDTO(clinicTotals, doctorSummaries);
    }

    // ================= CLINIC PAYMENTS BY DURATION =================
    // ================= CLINIC PAYMENTS BY DURATION =================
    public ClinicDurationPaymentDTO getClinicPayments(LocalDateTime from, LocalDateTime to) {

        // Call repository
        Object result = visitPaymentRepository.clinicSummaryByDate(
                from,
                to,
                PaymentMethod.CASH,
                PaymentMethod.POS,
                PaymentMethod.INSURANCE,
                PaymentMethod.FREE
        );

        // Safely unwrap result
        Object[] r = null;
        if (result instanceof Object[]) {
            r = (Object[]) result;
        } else if (result instanceof List) {
            List<?> list = (List<?>) result;
            if (!list.isEmpty() && list.get(0) instanceof Object[]) {
                r = (Object[]) list.get(0);
            }
        }

        // Default values if null
        double insuranceDiscount = 0.0;
        double cash = 0.0;
        double pos = 0.0;
        double insurance = 0.0;
        long freeVisits = 0L;

        if (r != null && r.length >= 4){
            cash = r[0] != null ? ((Number) r[0]).doubleValue() : 0.0;
            pos = r[1] != null ? ((Number) r[1]).doubleValue() : 0.0;
            insurance = r[2] != null ? ((Number) r[2]).doubleValue() : 0.0;
//            insuranceDiscount = r[3] != null ? ((Number) r[3]).doubleValue() : 0.0;
//            freeVisits = r[4] != null ? ((Number) r[4]).longValue() : 0L;

            insuranceDiscount = (r.length > 3 && r[3] != null)
                    ? ((Number) r[3]).doubleValue()
                    : 0.0;

            freeVisits = (r.length > 4 && r[4] != null)
                    ? ((Number) r[4]).longValue()
                    : 0L;

        }

        double total = cash + pos + (insurance - insuranceDiscount);

        return new ClinicDurationPaymentDTO(
                cash,
                pos,
                insurance,
                insuranceDiscount,
                freeVisits,
                total
        );


    }
    // ================= PER DOCTOR =================
//    public List<DoctorPaymentSummaryDTO> getDoctorSummary(LocalDateTime from, LocalDateTime to) {
//        return visitPaymentRepository.doctorPaymentSummary(
//                from,
//                to,
//                PaymentMethod.CASH,
//                PaymentMethod.POS,
//                PaymentMethod.INSURANCE,
//                PaymentMethod.FREE
//        );
//    }
    public List<DoctorPaymentSummaryDTO> getDoctorSummary(LocalDateTime from, LocalDateTime to) {
        // Fetch raw results from repository
        List<Object[]> results = visitPaymentRepository.doctorSummaryByDate(from, to);

        // Map to DTOs
        return results.stream().map(r -> {
            long doctorId = ((Number) r[0]).longValue();
            String doctorName = (String) r[1];
            double cash = r[2] != null ? ((Number) r[2]).doubleValue() : 0.0;
            double pos = r[3] != null ? ((Number) r[3]).doubleValue() : 0.0;
            double insurance = r[4] != null ? ((Number) r[4]).doubleValue() : 0.0;
            double insuranceDiscount = r[5] != null ? ((Number) r[5]).doubleValue() : 0.0;
            long freeVisits = r[6] != null ? ((Number) r[6]).longValue() : 0L;

            // Correct grand total: sum of only amounts, exclude free visits
            double grandTotal = cash + pos + (insurance - insuranceDiscount);
            return new DoctorPaymentSummaryDTO(
                    doctorId,
                    doctorName,
                    cash,
                    pos,
                    insurance,
                    insuranceDiscount,
                    freeVisits,
                    grandTotal
            );
        }).toList();
    }


    // ================= CLINIC TOTAL =================
//    public ClinicPaymentSummaryDTO getClinicSummary(LocalDateTime from, LocalDateTime to) {
//        Object[] r = visitPaymentRepository.clinicTotals(
//                from,
//                to,
//                PaymentMethod.CASH,
//                PaymentMethod.POS,
//                PaymentMethod.INSURANCE,
//                PaymentMethod.FREE
//        );
//
//        double cash = r[0] != null ? ((Number) r[0]).doubleValue() : 0.0;
//        double pos = r[1] != null ? ((Number) r[1]).doubleValue() : 0.0;
//        double insurance = r[2] != null ? ((Number) r[2]).doubleValue() : 0.0;
//        long freeVisits = r[3] != null ? ((Number) r[3]).longValue() : 0L;
//
//        double total = cash + pos + insurance;
//
//        return new ClinicPaymentSummaryDTO(cash, pos, insurance, freeVisits, total);
//    }


    // Search payments by acceptance number (case-insensitive)
    public List<VisitPayment> getByAcceptanceNumber(String acceptNumber) {
        if (acceptNumber == null || acceptNumber.isBlank()) {
            throw new RuntimeException("Acceptance number is required");
        }

        List<VisitPayment> payments = visitPaymentRepository.searchByAcceptNumberIgnoreCase(acceptNumber);

        if (payments.isEmpty()) {
            throw new RuntimeException("No payments found for acceptance number: " + acceptNumber);
        }

        return payments;
    }

}
