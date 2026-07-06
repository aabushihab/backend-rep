package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.choices.AppointmentStatus;
import com.clinic.doctor_app_backend.choices.PaymentMethod;
import com.clinic.doctor_app_backend.choices.VisitStatus;
import com.clinic.doctor_app_backend.dto.*;
import com.clinic.doctor_app_backend.model.*;
import com.clinic.doctor_app_backend.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitService {

    private final VisitDrugRepository visitDrugRepository;
    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final WalkInRepository walkInRepository; // Add this
    private final DrugRepository drugRepository;
    private final VisitPaymentRepository visitPaymentRepository;

    public VisitService(VisitDrugRepository visitDrugRepository, VisitRepository visitRepository,
                        PatientRepository patientRepository,
                        DoctorRepository doctorRepository,
                        AppointmentRepository appointmentRepository,
                        WalkInRepository walkInRepository, DrugRepository drugRepository, VisitPaymentRepository visitPaymentRepository) {
        this.visitDrugRepository = visitDrugRepository; // Update constructor
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.walkInRepository = walkInRepository;
        this.drugRepository = drugRepository;
        this.visitPaymentRepository = visitPaymentRepository;
    }
//    public List<?> getVisitsByPatientId(Long patientId) {
//        return visitRepository.findVisitsByPatient(patientId);
//    }



    public DoctorVisitSummaryDto getDoctorSummary(Long doctorId) {

        List<Visit> visits = visitRepository.findAllByDoctor_Id(doctorId);

        LocalDate today = LocalDate.now();

        DoctorVisitSummaryDto dto = new DoctorVisitSummaryDto();

        long totalVisits = visits.size();

        long totalClosed = 0;
        long totalOpen = 0;
        long totalToday = 0;
        long todayOpen = 0;

        for (Visit v : visits) {

            boolean isClosed = v.getVisitStatus() == VisitStatus.CLOSED;

            LocalDate visitDate = null;

            try {
                if (v.getVisitDate() != null) {
                    visitDate = v.getVisitDate().toLocalDate();
                }
            } catch (Exception ignored) {}

            // ---------------- TOTAL CLOSED ----------------
            if (isClosed) {
                totalClosed++;
            } else {
                totalOpen++;
            }

            // ---------------- TODAY ----------------
            if (visitDate != null && visitDate.equals(today)) {

                totalToday++;

                if (!isClosed) {
                    todayOpen++;
                }
            }
        }

        dto.setTotalVisits(totalVisits);
        dto.setTotalClosed(totalClosed);
        dto.setTotalOpen(totalOpen);
        dto.setTotalToday(totalToday);
        dto.setTodayOpen(todayOpen);

        return dto;
    }
    // ------------------- OPEN VISIT (with WalkIn linkage support) -------------------
    @Transactional
    public Visit openVisit(Long patientId, Long doctorId, Long appointmentId, String visitType, Long walkInId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = null;
        if (appointmentId != null) {
            appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
        }

        WalkInModel walkIn = null;
        if (walkInId != null) {
            walkIn = walkInRepository.findById(walkInId)
                    .orElseThrow(() -> new RuntimeException("Walk-in not found"));

            // Check if this walk-in already has a visit
            if (walkIn.getVisit() != null) {
                throw new RuntimeException("This walk-in already has an associated visit: " + walkIn.getVisit().getId());
            }
        }

        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setAppointment(appointment);
        visit.setWalkIn(walkIn); // Link to WalkInModel
        visit.setVisitType(visitType);
        visit.setVisitStatus(VisitStatus.CREATED);
        visit.setCheckInTime(LocalDateTime.now());
        visit.setCreatedAt(LocalDateTime.now());
        visit.setUpdatedAt(LocalDateTime.now());

        Visit savedVisit = visitRepository.save(visit);

        // Update the WalkInModel with the visit reference
        if (walkIn != null) {
            walkIn.setVisit(savedVisit);
            walkInRepository.save(walkIn);
        }

        return savedVisit;
    }

    // ------------------- OVERLOADED METHOD (backward compatibility) -------------------
    public Visit openVisit(Long patientId, Long doctorId, Long appointmentId, String visitType) {
        return openVisit(patientId, doctorId, appointmentId, visitType, null);
    }

    // ------------------- OPEN VISIT FROM WALK-IN -------------------
    @Transactional
    public Visit openVisitFromWalkIn(Long walkInId) {
        WalkInModel walkIn = walkInRepository.findById(walkInId)
                .orElseThrow(() -> new RuntimeException("Walk-in not found"));

        // Check if visit already exists
        if (walkIn.getVisit() != null) {
            return walkIn.getVisit(); // Return existing visit
        }

        // Create visit from walk-in
        Visit visit = new Visit();
        visit.setWalkIn(walkIn);
        visit.setPatient(walkIn.getPatient());
        visit.setDoctor(walkIn.getDoctor());
        visit.setVisitType(walkIn.getType() != null ? walkIn.getType().toString() : "WALK_IN");
        visit.setVisitStatus(VisitStatus.CREATED);
        visit.setCheckInTime(walkIn.getVisitTime() != null ? walkIn.getVisitTime() : LocalDateTime.now());
        visit.setCreatedAt(LocalDateTime.now());
        visit.setUpdatedAt(LocalDateTime.now());

        Visit savedVisit = visitRepository.save(visit);

        // Update walk-in with visit reference
        walkIn.setVisit(savedVisit);
        walkInRepository.save(walkIn);

        return savedVisit;
    }
    public List<Visit> searchVisitsByDoctorAndName(
            Long doctorId,
            String name
    ) {
        return visitRepository.searchByDoctorAndPatientName(
                doctorId,
                name
        );
    }


    public Visit getVisitById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
    }


    public Map<Long, List<Visit>> getVisitsByPatientIds(List<Long> patientIds) {

        List<Visit> visits = visitRepository.findByPatientIdIn(patientIds);

        return visits.stream()
                .collect(Collectors.groupingBy(v -> v.getPatient().getId()));
    }
    // ------------------- GET VISIT BY WALK-IN ID -------------------
    public Optional<Visit> getVisitByWalkInId(Long walkInId) {
        return visitRepository.findByWalkInId(walkInId);
    }

    // ------------------- CHECK IF WALK-IN HAS VISIT -------------------
    public boolean hasVisitForWalkIn(Long walkInId) {
        return visitRepository.existsByWalkInId(walkInId);
    }

    // ------------------- START VISIT FROM DOCTOR -------------------
    @Transactional
    public Visit startVisitFromDoctor(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
        visit.setVisitStatus(VisitStatus.IN_PROGRESS);
        visit.setConsultationStart(LocalDateTime.now());
        visit.setUpdatedAt(LocalDateTime.now());

        // Also update the associated walk-in status if it exists
        if (visit.getWalkIn() != null) {
            WalkInModel walkIn = visit.getWalkIn();
            walkIn.markInProgress(walkIn.getDoctor(), walkIn.getRoom());
            walkIn.setStatus(VisitStatus.CLOSED);
            walkIn.setClosedAt(LocalDateTime.now());
            walkInRepository.save(walkIn);
        }
        if (visit.getAppointment()!= null){
            Appointment appointment = visit.getAppointment();
            appointment.setStatus(AppointmentStatus.CLOSED);
            appointment.setClosedDate(LocalDateTime.now());
            appointmentRepository.save(appointment);
        }

        return visitRepository.save(visit);
    }

    // ------------------- END VISIT FROM DOCTOR -------------------
    @Transactional
    public Visit endVisitFromDoctor(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
        visit.setVisitStatus(VisitStatus.CLOSED);
        visit.setConsultationEnd(LocalDateTime.now());
        visit.setUpdatedAt(LocalDateTime.now());

        // Also update the associated walk-in status if it exists
        if (visit.getWalkIn() != null) {
            WalkInModel walkIn = visit.getWalkIn();
            walkIn.markClosed();
            walkInRepository.save(walkIn);
        }
        if (visit.getAppointment()!= null){
            Appointment appointment = visit.getAppointment();
            appointment.setStatus(AppointmentStatus.CLOSED);
            appointment.setClosedDate(LocalDateTime.now());
            appointmentRepository.save(appointment);
        }

        return visitRepository.save(visit);
    }

    // ------------------- CLOSE VISIT FROM DOCTOR -------------------
    @Transactional
    public Visit closeVisitFromDoctor(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
        visit.setVisitStatus(VisitStatus.CLOSED);
        visit.setConsultationEnd(LocalDateTime.now());
        visit.setUpdatedAt(LocalDateTime.now());

        // Also update the associated walk-in status if it exists
        if (visit.getWalkIn() != null) {
            WalkInModel walkIn = visit.getWalkIn();
            walkIn.markClosed();
            walkInRepository.save(walkIn);
        }

        return visitRepository.save(visit);
    }



    // ------------------- UPDATE VISIT DETAILS -------------------
    @Transactional
    public Visit updateVisitDetails(Long visitId,
                                    String chiefComplaint,
                                    String history,
                                    String medications,
                                    String allergies,
                                    String doctorNotes) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        if (chiefComplaint != null) visit.setChiefComplaint(chiefComplaint);
        if (history != null) visit.setHistory(history);
        if (medications != null) visit.setMedications(medications);
        if (allergies != null) visit.setAllergies(allergies);
        if (doctorNotes != null) visit.setDoctorNotes(doctorNotes);

        visit.setUpdatedAt(LocalDateTime.now());

        return visitRepository.save(visit);
    }

    public Optional<Visit> getVisit(Long id) {
        return visitRepository.findById(id);
    }

    public List<Visit> getVisitsByPatient(Long patientId) {
        return visitRepository.findByPatientId(patientId);
    }

//    public Page<Visit> getVisitsByDoctor(
//            Long doctorId,
//            int page,
//            int size
//    ) {
//        return visitRepository.findByDoctorId(
//                doctorId,
//                PageRequest.of(page, size)
//        );
//    }

    public Page<Visit> getVisitsByDoctor(Long doctorId, int page, int size) {
        return visitRepository.findVisitsByDoctorOrdered(
                doctorId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        );
    }


    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    // ------------------- GET VISITS WITH WALK-IN INFO -------------------
    public List<Visit> getVisitsWithWalkInInfo() {
        return visitRepository.findAllByWalkInIsNotNull();
    }

    // ------------------- GET VISITS WITHOUT WALK-IN -------------------
    public List<Visit> getVisitsWithoutWalkIn() {
        return visitRepository.findAllByWalkInIsNull();
    }


    // ---------------- SAVE MEDICAL DETAILS ----------------
//    public Visit updateVisitDetails(Long visitId, VisitDetailsRequest request) {
//
//        Visit visit = visitRepository.findById(visitId)
//                .orElseThrow(() -> new RuntimeException("Visit not found"));
//
//        if (visit.getVisitStatus() == VisitStatus.CLOSED) {
//            throw new RuntimeException("Cannot update closed visit");
//        }
//
//        visit.setChiefComplaint(request.getChiefComplaint());
//        visit.setHistory(request.getHistory());
//        visit.setMedications(request.getMedications());
//        visit.setAllergies(request.getAllergies());
//        visit.setDoctorNotes(request.getDoctorNotes());
//        visit.setUpdatedAt(LocalDateTime.now());
//
//        return visitRepository.save(visit);
//    }
//    @Transactional(readOnly = true)

    public Visit updateVisitDetails(Long visitId, VisitDetailsRequest request) {

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        if (visit.getVisitStatus() == VisitStatus.CLOSED) {
            throw new RuntimeException("Cannot update closed visit");
        }

        visit.setChiefComplaint(request.getChiefComplaint());
        visit.setHistory(request.getHistory());
        visit.setMedications(request.getMedications());
        visit.setAllergies(request.getAllergies());
        visit.setDoctorNotes(request.getDoctorNotes());

        // ---------------- ADD DISCHARGE STATUS ----------------
        if (request.getDischargeStatus() != null) {
            visit.setDischargeStatus(request.getDischargeStatus());
        }

        visit.setUpdatedAt(LocalDateTime.now());

        return visitRepository.save(visit);
    }


//    public VisitPaymentDetailsResponse getPaymentDetails(Long visitId) {
//
//        Visit visit = visitRepository.findById(visitId)
//                .orElseThrow(() -> new RuntimeException("Visit not found"));
//
//        double totalPaid = visit.getPayments()
//                .stream()
//                .mapToDouble(VisitPayment::getAmount)
//                .sum();
//
//        double original = visit.getOriginalAmount() != null
//                ? visit.getOriginalAmount()
//                : 0.0;
//
//        VisitPaymentDetailsResponse res = new VisitPaymentDetailsResponse();
//        res.setVisitId(visit.getId());
//        res.setOriginalAmount(original);
//        res.setTotalPaid(totalPaid);
//        res.setRemainingAmount(original - totalPaid);
//        res.setPaid(visit.isPaid());
//
//        List<VisitPaymentDetailsResponse.PaymentItem> items =
//                visit.getPayments().stream().map(p -> {
//
//                    VisitPaymentDetailsResponse.PaymentItem i =
//                            new VisitPaymentDetailsResponse.PaymentItem();
//
//                    i.setId(p.getId());
//                    i.setPaymentMethod(p.getPaymentMethod());
//                    i.setAmount(p.getAmount());
//
//                    i.setTerminalId(p.getTerminalId());
//                    i.setReferenceNumber(p.getReferenceNumber());
//                    i.setCardType(p.getCardType());
//                    i.setApprovalCode(p.getApprovalCode());
//
//                    i.setPaidAt(p.getPaidAt());
//                    return i;
//                }).toList();
//
//        res.setPayments(items);
//        return res;
//    }






//    @Transactional(readOnly = true)
//    public VisitPaymentDetailsResponse getPaymentDetails(Long visitId) {
//
//        Visit visit = visitRepository.findById(visitId)
//                .orElseThrow(() -> new RuntimeException("Visit not found"));
//
//        double patientPaid = visit.getPayments().stream()
//                .filter(p -> p.getPaymentMethod() == PaymentMethod.CASH
//                        || p.getPaymentMethod() == PaymentMethod.POS)
//                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
//                .sum();
//
//        double insuranceCovered = visit.getPayments().stream()
//                .filter(p -> p.getPaymentMethod() == PaymentMethod.INSURANCE)
//                .mapToDouble(p -> p.getInsuranceAmount() != null ? p.getInsuranceAmount() : 0.0)
//                .sum();
//
//        double original = visit.getOriginalAmount() != null
//                ? visit.getOriginalAmount()
//                : 0.0;
//
//        VisitPaymentDetailsResponse res = new VisitPaymentDetailsResponse();
//        res.setVisitId(visit.getId());
//        res.setCurrency(visit.getCurrency());
//        res.setOriginalAmount(original);
//        res.setPatientPaid(patientPaid);
//        res.setInsuranceAmount(insuranceCovered);
//        res.setRemainingAmount(
//                Math.max(0, original - patientPaid - insuranceCovered)
//        );
//        res.setPaid(visit.isPaid());
//
//        List<VisitPaymentDetailsResponse.PaymentItem> items =
//                visit.getPayments().stream().map(p -> {
//
//                    VisitPaymentDetailsResponse.PaymentItem i =
//                            new VisitPaymentDetailsResponse.PaymentItem();
//
//                    i.setId(p.getId());
//                    i.setPaymentMethod(p.getPaymentMethod());
//
//                    // CASH / POS
//                    i.setAmount(p.getAmount());
//
//                    // INSURANCE
//                    i.setInsuranceAmount(p.getInsuranceAmount());
//                    i.setCoveragePercent(p.getCoveragePercent());
//                    i.setInsuranceProvider(p.getInsuranceProvider());
//                    i.setInsuranceClass(p.getInsuranceClass());
//                    i.setInsuranceType(p.getInsuranceType());
//                    i.setInsuranceAcceptNumber(p.getInsuranceAcceptNumber());
//
//                    // POS
//                    i.setTerminalId(p.getTerminalId());
//                    i.setReferenceNumber(p.getReferenceNumber());
//                    i.setCardType(p.getCardType());
//                    i.setApprovalCode(p.getApprovalCode());
//
//                    i.setPaidAt(p.getPaidAt());
//                    return i;
//                }).toList();
//
//        res.setPayments(items);
//
//        return res;
//    }


    @Transactional(readOnly = true)
    public VisitPaymentDetailsResponse getPaymentDetails(Long visitId) {

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        double patientPaid = visit.getPayments().stream()
                .filter(p -> p.getPaymentMethod() == PaymentMethod.CASH
                        || p.getPaymentMethod() == PaymentMethod.POS)
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        // ================= INSURANCE LOGIC (UPDATED) =================
        double insurancePaidAmount = visit.getPayments().stream()
                .filter(p -> p.getPaymentMethod() == PaymentMethod.INSURANCE)
                .mapToDouble(p -> p.getInsurancePaidAmount() != null ? p.getInsurancePaidAmount() : 0.0)
                .sum();

        double insuranceDiscount = visit.getPayments().stream()
                .filter(p -> p.getPaymentMethod() == PaymentMethod.INSURANCE)
                .mapToDouble(p -> p.getInsuranceDiscount() != null ? p.getInsuranceDiscount() : 0.0)
                .sum();

        double insuranceAmount = visit.getPayments().stream()
                .filter(p -> p.getPaymentMethod() == PaymentMethod.INSURANCE)
                .mapToDouble(p -> p.getInsuranceAmount() != null ? p.getInsuranceAmount() : 0.0)
                .sum();

        double original = visit.getOriginalAmount() != null
                ? visit.getOriginalAmount()
                : 0.0;

        // ================= RESPONSE =================
        VisitPaymentDetailsResponse res = new VisitPaymentDetailsResponse();
        res.setVisitId(visit.getId());
        res.setCurrency(visit.getCurrency());
        res.setOriginalAmount(original);

        res.setPatientPaid(patientPaid);

        // keep for backward compatibility
        res.setInsuranceAmount(insuranceAmount);

        // NEW REQUIRED VALUES (IMPORTANT FOR YOUR UI RULE)
        res.setInsurancePaidAmount(insurancePaidAmount);
        res.setInsuranceDiscount(insuranceDiscount);

        // ================= REMAINING =================
        double insuranceTotal = insurancePaidAmount + insuranceDiscount;

        res.setRemainingAmount(
                Math.max(0, original - patientPaid - insuranceTotal)
        );

        res.setPaid(visit.isPaid());

        // ================= PAYMENT ITEMS =================
        List<VisitPaymentDetailsResponse.PaymentItem> items =
                visit.getPayments().stream().map(p -> {

                    VisitPaymentDetailsResponse.PaymentItem i =
                            new VisitPaymentDetailsResponse.PaymentItem();

                    i.setId(p.getId());
                    i.setPaymentMethod(p.getPaymentMethod());

                    // CASH / POS
                    i.setAmount(p.getAmount());

                    // INSURANCE (FULL DATA REQUIRED)
                    i.setInsuranceAmount(p.getInsuranceAmount());
                    i.setInsurancePaidAmount(p.getInsurancePaidAmount());
                    i.setInsuranceDiscount(p.getInsuranceDiscount());

                    i.setCoveragePercent(p.getCoveragePercent());
                    i.setInsuranceProvider(p.getInsuranceProvider());
                    i.setInsuranceClass(p.getInsuranceClass());
                    i.setInsuranceType(p.getInsuranceType());
                    i.setInsuranceAcceptNumber(p.getInsuranceAcceptNumber());

                    // POS
                    i.setTerminalId(p.getTerminalId());
                    i.setReferenceNumber(p.getReferenceNumber());
                    i.setCardType(p.getCardType());
                    i.setApprovalCode(p.getApprovalCode());

                    i.setPaidAt(p.getPaidAt());

                    return i;
                }).toList();

        res.setPayments(items);

        return res;
    }
//
//    @Transactional
//    public void payVisit(Long visitId, VisitPayRequest req) {
//
//        Visit visit = visitRepository.findById(visitId)
//                .orElseThrow(() -> new RuntimeException("Visit not found"));
//
//        // Set original amount only once
//        if (visit.getOriginalAmount() == null) {
//            if (req.getOriginalAmount() == null || req.getOriginalAmount() <= 0) {
//                throw new RuntimeException("Original amount required");
//            }
//            visit.setOriginalAmount(req.getOriginalAmount());
//        }
//
//        if (req.getPayments() == null || req.getPayments().isEmpty()) {
//            throw new RuntimeException("At least one payment is required");
//        }
//
//        double alreadyPaid = visit.getPayments().stream()
//                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0)
//                .sum();
//
//        double newPaid = 0;
//
//        for (VisitPayRequest.PaymentPart part : req.getPayments()) {
//            VisitPayment payment = new VisitPayment();
//            payment.setVisit(visit);
//            payment.setPaymentMethod(part.getPaymentMethod());
//
//            switch (part.getPaymentMethod()) {
//                case CASH -> {
//                    if (part.getCashAmount() == null || part.getCashAmount() <= 0) {
//                        throw new RuntimeException("Cash amount required");
//                    }
//                    payment.setAmount(part.getCashAmount());
//                    newPaid += part.getCashAmount();
//                }
//                case POS -> {
//                    if (part.getCashAmount() == null || part.getCashAmount() <= 0) {
//                        throw new RuntimeException("POS amount required");
//                    }
//                    if (part.getTerminalId() == null || part.getPosPaymentNo() == null
//                            || part.getCardType() == null || part.getApprovalCode() == null) {
//                        throw new RuntimeException("All POS fields are required");
//                    }
//                    payment.setAmount(part.getCashAmount());
//                    payment.setTerminalId(part.getTerminalId());
//                    payment.setReferenceNumber(part.getPosPaymentNo());
//                    payment.setCardType(part.getCardType());
//                    payment.setApprovalCode(part.getApprovalCode());
//                    newPaid += part.getCashAmount();
//                }
//                case FREE -> payment.setAmount(0.0);
//
//                case INSURANCE -> {
//                    if (part.getCoveragePercent() == null || part.getCoveragePercent() <= 0) {
//                        throw new RuntimeException("Insurance coverage percent required");
//                    }
//                    if (part.getInsuranceAmount() == null || part.getInsuranceAmount() <= 0) {
//                        throw new RuntimeException("Insurance amount required");
//                    }
//
//                    double coverage = part.getCoveragePercent() / 100.0;
//                    double insuredAmount = part.getInsuranceAmount() * coverage;       // insurance covers this
//                    double cashAmount = part.getInsuranceAmount() - insuredAmount;     // patient pays this
//
//                    // ----- Payment 1: Insurance -----
//                    VisitPayment insurancePayment = new VisitPayment();
//                    insurancePayment.setVisit(visit);
//                    insurancePayment.setPaymentMethod(PaymentMethod.INSURANCE);
//                    insurancePayment.setInsuranceAmount(insuredAmount);
//                    insurancePayment.setInsuranceProvider(part.getInsuranceProvider());
//                    insurancePayment.setInsuranceClass(part.getInsuranceClass());
//                    insurancePayment.setInsuranceType(part.getInsuranceType());
//                    insurancePayment.setCoveragePercent(part.getCoveragePercent());
//                    insurancePayment.setInsuranceAcceptNumber(part.getInsuranceAcceptNumber());
//                    insurancePayment.setCardId(part.getCardId());
//                    insurancePayment.setInsuranceFormId(part.getInsuranceFormId());
//                    insurancePayment.setAmount(insuredAmount); // cash part is 0
//                    visit.getPayments().add(insurancePayment);
//
//                    // ----- Payment 2: Cash for remaining -----
//                    if (cashAmount >= 0) {
//                        VisitPayment cashPayment = new VisitPayment();
//                        cashPayment.setVisit(visit);
//                        cashPayment.setPaymentMethod(PaymentMethod.CASH);
//                        cashPayment.setAmount(cashAmount);
//                        visit.getPayments().add(cashPayment);
//                        newPaid += cashAmount; // count toward alreadyPaid
//                    }
//                }
//
//                default -> throw new RuntimeException("Unsupported payment method");
//            }
//
//            visit.getPayments().add(payment);
//        }
//
//        double remaining = visit.getOriginalAmount() - alreadyPaid - newPaid;
//        if (remaining < 0) {
//            throw new RuntimeException("Payment exceeds remaining balance");
//        }
//
//        // Close visit if fully paid
//        if (alreadyPaid + newPaid >= visit.getOriginalAmount()) {
//            visit.setPaid(true);
//            visit.setPaiedAt(LocalDateTime.now());
//        }
//
//        visitRepository.save(visit);
//    }

    @Transactional
    public Visit reopenVisit(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("The Visit Is Not Exist"));

        visit.setVisitStatus(VisitStatus.IN_PROGRESS);
        return visit; // JPA auto-flushes on transaction commit
    }

//    public Visit addDrugsToVisit(Long visitId, List<Long> drugIds) {
//
//        Visit visit = visitRepository.findById(visitId)
//                .orElseThrow(() -> new RuntimeException("Visit not found"));
//
//        List<Drug> newDrugs = drugRepository.findAllById(drugIds);
//
//        // prevent duplicates
//        List<Drug> existing = visit.getDrugs();
//
//        for (Drug d : newDrugs) {
//            if (!existing.contains(d)) {
//                existing.add(d);
//            }
//        }
//
//        return visitRepository.save(visit);
//    }


    public VisitDrug updateVisitDrugDetails(
            Long visitId,
            VisitDrugDetailsRequest req
    ) {

        VisitDrug vd = visitDrugRepository
                .findByVisit_IdAndDrug_DrugId(visitId, req.getDrugId())
                .orElseThrow(() -> new RuntimeException("VisitDrug not found"));

        vd.setDuration(req.getDuration());
        vd.setDurationType(String.valueOf(req.getDurationType()));
        vd.setFrequency(req.getFrequency());
        vd.setDose(req.getDose());
        vd.setInstructions(req.getInstruction());

        return visitDrugRepository.save(vd);
    }

    @Transactional(readOnly = true)
    public List<VisitDrugPrescriptionDto> getDrugsPrescriptionByVisitId(Long visitId) {

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        return visit.getVisitDrugs()
                .stream()
                .map(vd -> {

                    Drug drug = vd.getDrug();

                    VisitDrugPrescriptionDto dto =
                            new VisitDrugPrescriptionDto();

                    dto.setDrugId(drug.getDrugId());
                    dto.setTradeName(drug.getTradeName());
                    dto.setStrength(drug.getStrength());
                    dto.setPackageSize(drug.getPackageSize());
                    dto.setDosageUnit(drug.getDosageUnit());

                    dto.setDuration(vd.getDuration());
                    dto.setDurationType(vd.getDurationType());
                    dto.setFrequency(vd.getFrequency());
                    dto.setDose(vd.getDose());
                    dto.setInstructions(vd.getInstructions());

                    return dto;
                })
                .toList();
    }


public Visit addDrugsToVisit(Long visitId, List<Long> drugIds) {

    Visit visit = visitRepository.findById(visitId)
            .orElseThrow(() -> new RuntimeException("Visit not found"));

    List<Drug> drugs = drugRepository.findAllById(drugIds);

    for (Drug drug : drugs) {

        boolean exists = visit.getVisitDrugs()
                .stream()
                .anyMatch(vd ->
                        vd.getDrug().getDrugId().equals(drug.getDrugId())
                );

        if (!exists) {
            VisitDrug visitDrug = new VisitDrug();
            visitDrug.setVisit(visit);
            visitDrug.setDrug(drug);

            visit.getVisitDrugs().add(visitDrug);
        }
    }

    return visitRepository.save(visit);
}
//    public Visit removeDrug(Long visitId, Long drugId) {
//
//        Visit visit = visitRepository.findById(visitId)
//                .orElseThrow(() -> new RuntimeException("Visit not found"));
//
//        visit.getDrugs().removeIf(d -> d.getDrugId().equals(drugId));
//
//        return visitRepository.save(visit);
//    }


    public Visit removeDrug(Long visitId, Long drugId) {

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        visit.getVisitDrugs().removeIf(vd ->
                vd.getDrug().getDrugId().equals(drugId)
        );

        return visitRepository.save(visit);
    }


//    @Transactional
//    public void settleInsurance(
//            Long paymentId,
//            Double paidAmount,
//            Double discountAmount
//    ) {
//
//        VisitPayment payment = visitPaymentRepository.findById(paymentId)
//                .orElseThrow(() -> new RuntimeException("Payment not found"));
//
//        payment.setInsurancePaidAmount(paidAmount);
//        payment.setInsuranceDiscount(discountAmount);
//
//        double totalSettled =
//                (paidAmount == null ? 0 : paidAmount) +
//                        (discountAmount == null ? 0 : discountAmount);
//
//        double expected = payment.getInsuranceAmount() == null
//                ? 0
//                : payment.getInsuranceAmount();
//
//        boolean settled = totalSettled >= expected;
//
//        payment.setInsurancePaid(settled);
//
//        // IMPORTANT: persist payment
//        visitPaymentRepository.save(payment);
//
//        Visit visit = payment.getVisit();
//
//        double totalPaid = visit.getPayments().stream()
//                .mapToDouble(p ->
//                        (p.getAmount() != null ? p.getAmount() : 0)
//                                +
//                                (p.getInsurancePaidAmount() != null ? p.getInsurancePaidAmount() : 0)
//                                +
//                                (p.getInsuranceDiscount() != null ? p.getInsuranceDiscount() : 0)
//                )
//                .sum();
//
//        if (totalPaid >= visit.getOriginalAmount()) {
//            visit.setPaid(true);
//            visit.setPaiedAt(LocalDateTime.now());
//
//            // 🔥 FIX: close visit when fully settled
//            visit.setVisitStatus(VisitStatus.CLOSED);
//
//        }
//
//        visitRepository.save(visit);
//    } above 20062026


    @Transactional
    public void settleInsurance(
            Long paymentId,
            Double paidAmount,
            Double discountAmount
    ) {

        VisitPayment payment = visitPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        double paid = paidAmount == null ? 0 : paidAmount;
        double discount = discountAmount == null ? 0 : discountAmount;
        double expected = payment.getInsuranceAmount() == null ? 0 : payment.getInsuranceAmount();

        // ================= UPDATE PAYMENT =================
        payment.setInsurancePaidAmount(paid);
        payment.setInsuranceDiscount(discount);

        boolean insuranceFullySettled =
                Math.abs((paid + discount) - expected) < 0.0001;

        payment.setInsurancePaid(insuranceFullySettled);

        visitPaymentRepository.save(payment);

        // ================= UPDATE VISIT =================
        Visit visit = payment.getVisit();

        // 🔥 ONLY insurance decides closure (no mixing with cash/POS)
        if (insuranceFullySettled) {
            visit.setPaid(true);
            visit.setPaiedAt(LocalDateTime.now());
            visit.setVisitStatus(VisitStatus.CLOSED);
        }

        visitRepository.save(visit);
    }




    @Transactional
    public void payVisit(Long visitId, VisitPayRequest req) {

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        // ================= ORIGINAL AMOUNT + CURRENCY =================
        if (visit.getOriginalAmount() == null) {

            if (req.getOriginalAmount() == null || req.getOriginalAmount() <= 0) {
                throw new RuntimeException("Original amount required");
            }

            if (req.getCurrency() == null || req.getCurrency().isBlank()) {
                throw new RuntimeException("Currency is required");
            }

            visit.setOriginalAmount(req.getOriginalAmount());
            visit.setCurrency(req.getCurrency());
        }

        if (req.getPayments() == null || req.getPayments().isEmpty()) {
            throw new RuntimeException("At least one payment is required");
        }

        // ================= CALCULATE TOTAL PAID =================
        double totalPaid = visit.getPayments().stream()
                .mapToDouble(p -> {

                    double cashPos = p.getAmount() != null ? p.getAmount() : 0.0;

                    double insuranceSettled =
                            (p.getInsurancePaidAmount() != null ? p.getInsurancePaidAmount() : 0.0)
                                    + (p.getInsuranceDiscount() != null ? p.getInsuranceDiscount() : 0.0);

                    return cashPos + insuranceSettled;
                })
                .sum();

        // ================= PROCESS NEW PAYMENTS =================
        for (VisitPayRequest.PaymentPart part : req.getPayments()) {

            switch (part.getPaymentMethod()) {

                // ---------------- CASH ----------------
                case CASH -> {
                    if (part.getCashAmount() == null || part.getCashAmount() <= 0) {
                        throw new RuntimeException("Cash amount required");
                    }

                    VisitPayment payment = new VisitPayment();
                    payment.setVisit(visit);
                    payment.setPaymentMethod(PaymentMethod.CASH);
                    payment.setAmount(part.getCashAmount());

                    visit.getPayments().add(payment);

                    totalPaid += part.getCashAmount();
                }

                // ---------------- POS ----------------
                case POS -> {
                    if (part.getCashAmount() == null || part.getCashAmount() <= 0) {
                        throw new RuntimeException("POS amount required");
                    }

                    if (part.getTerminalId() == null ||
                            part.getPosPaymentNo() == null ||
                            part.getCardType() == null ||
                            part.getApprovalCode() == null) {
                        throw new RuntimeException("All POS fields are required");
                    }

                    VisitPayment payment = new VisitPayment();
                    payment.setVisit(visit);
                    payment.setPaymentMethod(PaymentMethod.POS);
                    payment.setAmount(part.getCashAmount());

                    payment.setTerminalId(part.getTerminalId());
                    payment.setReferenceNumber(part.getPosPaymentNo());
                    payment.setCardType(part.getCardType());
                    payment.setApprovalCode(part.getApprovalCode());

                    visit.getPayments().add(payment);

                    totalPaid += part.getCashAmount();
                }

                // ---------------- FREE ----------------
                case FREE -> {
                    VisitPayment payment = new VisitPayment();
                    payment.setVisit(visit);
                    payment.setPaymentMethod(PaymentMethod.FREE);
                    payment.setAmount(0.0);

                    visit.getPayments().add(payment);
                }

                // ---------------- INSURANCE ----------------
                case INSURANCE -> {

                    if (part.getCoveragePercent() == null || part.getCoveragePercent() <= 0) {
                        throw new RuntimeException("Insurance coverage percent required");
                    }

                    if (part.getInsuranceAmount() == null || part.getInsuranceAmount() <= 0) {
                        throw new RuntimeException("Insurance amount required");
                    }

                    double coverage = part.getCoveragePercent() / 100.0;

                    double totalAmount = part.getInsuranceAmount();

                    double insuredAmount = totalAmount * coverage;   // insurance pays
                    double cashAmount = totalAmount - insuredAmount; // patient pays

                    // ================= INSURANCE RECORD =================
                    VisitPayment insurancePayment = new VisitPayment();
                    insurancePayment.setVisit(visit);
                    insurancePayment.setPaymentMethod(PaymentMethod.INSURANCE);

                    insurancePayment.setInsuranceAmount(insuredAmount);
                    insurancePayment.setInsurancePaid(false);
                    insurancePayment.setInsurancePaidAmount(0.0);
                    insurancePayment.setInsuranceDiscount(0.0);

                    insurancePayment.setInsuranceProvider(part.getInsuranceProvider());
                    insurancePayment.setInsuranceClass(part.getInsuranceClass());
                    insurancePayment.setInsuranceType(part.getInsuranceType());
                    insurancePayment.setCoveragePercent(part.getCoveragePercent());
                    insurancePayment.setInsuranceAcceptNumber(part.getInsuranceAcceptNumber());
                    insurancePayment.setInsuranceCardId(part.getCardId());
                    insurancePayment.setInsuranceFormId(part.getInsuranceFormId());

                    visit.getPayments().add(insurancePayment);

                    // ================= CASH PART =================
                    if (cashAmount > 0) {
                        VisitPayment cashPayment = new VisitPayment();
                        cashPayment.setVisit(visit);
                        cashPayment.setPaymentMethod(PaymentMethod.CASH);
                        cashPayment.setAmount(cashAmount);

                        visit.getPayments().add(cashPayment);

                        totalPaid += cashAmount;
                    }
                }

                default -> throw new RuntimeException("Unsupported payment method");
            }
        }

        // ================= FINAL PAYMENT CHECK =================
        double original = visit.getOriginalAmount() == null ? 0.0 : visit.getOriginalAmount();

        if (totalPaid >= original) {
            visit.setPaid(true);
            visit.setPaiedAt(LocalDateTime.now());
        }

        visitRepository.save(visit);
    }

    public List<DrugDto> getDrugsByVisitId(Long visitId) {

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        return visit.getVisitDrugs().stream()
                .map(vd -> {
                    Drug d = vd.getDrug();

                    DrugDto dto = new DrugDto();
                    dto.setDrugId(d.getDrugId());
                    dto.setTradeName(d.getTradeName());
                    dto.setStrength(d.getStrength());
                    dto.setPackageSize(d.getPackageSize());
                    dto.setDosageUnit(d.getDosageUnit());

                    return dto;
                })
                .toList();
    }


    // ✅ GET ALL procedures of a visit
    public List<String> getProcedures(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        return visit.getProcedures();
    }

    // ✅ ADD one procedure
    public List<String> addProcedure(Long visitId, String procedure) {

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        String value = procedure.trim();

        boolean exists = visit.getProcedures()
                .stream()
                .anyMatch(p -> p.equalsIgnoreCase(value));

        if (exists) {
            throw new RuntimeException("Procedure already exists");
        }
        visit.getProcedures().add(value);
        visitRepository.save(visit);

        return visit.getProcedures();
    }

    // ✅ DELETE one procedure
    public List<String> deleteProcedure(Long visitId, String procedure) {

        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        String value = procedure.trim();

        boolean exists = visit.getProcedures()
                .stream()
                .anyMatch(p -> p.equalsIgnoreCase(value));

        if (!exists) {
            throw new RuntimeException("Procedure not found");
        }

        visit.getProcedures()
                .removeIf(p -> p.equalsIgnoreCase(value));

        visitRepository.save(visit);

        return visit.getProcedures();
    }
    // ✅ UPDATE procedure (replace old with new)
    public List<String> updateProcedure(Long visitId, String oldProcedure, String newProcedure) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        List<String> procedures = visit.getProcedures();

        int index = procedures.indexOf(oldProcedure);
        if (index == -1) {
            throw new RuntimeException("Procedure not found");
        }

        procedures.set(index, newProcedure);
        visitRepository.save(visit);

        return procedures;
    }

















    public List<VisitPayment> getAllInsurancePayments() {
        return visitPaymentRepository.findAllInsurancePayments();
    }

    public List<VisitPayment> getUnpaidInsuranceClaims() {
        return visitPaymentRepository.findUnpaidInsuranceClaims();
    }

    public List<VisitPayment> getOutstandingInsuranceClaims() {
        return visitPaymentRepository.findOutstandingInsuranceClaims();
    }

    public List<VisitPayment> getInsurancePaymentsByProvider(String provider) {
        return visitPaymentRepository.findByInsuranceProvider(provider);
    }

    public VisitPayment getInsuranceClaimByAcceptNumber(String acceptNumber) {
        return visitPaymentRepository.findByInsuranceAcceptNumber(acceptNumber)
                .orElseThrow(() ->
                        new RuntimeException("Insurance claim not found"));
    }

    public List<Object[]> getInsuranceSummaryByProvider() {
        return visitPaymentRepository.insuranceSummaryByProvider();
    }

    public List<VisitPayment> getInsurancePaymentsBetweenDates(
            LocalDate fromDate,
            LocalDate toDate
    ) {
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = toDate.atTime(LocalTime.MAX);

        return visitPaymentRepository.findInsurancePaymentsBetweenDates(from, to);
    }
    public List<VisitPayment> getByIds(List<Long> ids) {

        Map<Long, VisitPayment> map =
                visitPaymentRepository.findAllById(ids)
                        .stream()
                        .collect(Collectors.toMap(VisitPayment::getId, v -> v));

        return ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .toList();
    }


    private VisitPaymentFullDto toDto(VisitPayment p) {

        VisitPaymentFullDto d = new VisitPaymentFullDto();

        d.id = p.getId();
        d.visitId = p.getVisit() != null ? p.getVisit().getId() : null;

        d.paymentMethod = p.getPaymentMethod() != null ? p.getPaymentMethod().name() : null;

        d.amount = p.getAmount();

        d.insuranceAmount = p.getInsuranceAmount();
        d.insuranceProvider = p.getInsuranceProvider();
        d.insuranceClass = p.getInsuranceClass();
        d.insuranceType = p.getInsuranceType();
        d.coveragePercent = p.getCoveragePercent();

        d.insuranceAcceptNumber = p.getInsuranceAcceptNumber();
        d.insuranceCardId = p.getInsuranceCardId();
        d.insuranceFormId = p.getInsuranceFormId();

        d.insurancePaid = p.getInsurancePaid();
        d.insurancePaidAmount = p.getInsurancePaidAmount();
        d.insuranceDiscount = p.getInsuranceDiscount();

        d.terminalId = p.getTerminalId();
        d.referenceNumber = p.getReferenceNumber();
        d.cardType = p.getCardType();
        d.approvalCode = p.getApprovalCode();

        d.paidAt = p.getPaidAt();

        double billed = p.getInsuranceAmount() == null ? 0 : p.getInsuranceAmount();
        double paid = p.getInsurancePaidAmount() == null ? 0 : p.getInsurancePaidAmount();
        double discount = p.getInsuranceDiscount() == null ? 0 : p.getInsuranceDiscount();

        d.outstanding = billed - paid - discount;
        d.settled = (paid + discount) >= billed;

        return d;
    }



    public int findPageOfVisit(Long doctorId, Long visitId, int pageSize) {
        long countBefore = visitRepository.countVisitsBefore(doctorId, visitId);
        return (int) (countBefore / pageSize);
    }

}