package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.choices.PatientType;
import com.clinic.doctor_app_backend.choices.PaymentMethod;
import com.clinic.doctor_app_backend.dto.PatientPaymentDTO;
import com.clinic.doctor_app_backend.model.Patient;
import com.clinic.doctor_app_backend.model.VisitPayment;
import com.clinic.doctor_app_backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientPaymentDTO> getPaymentsByMethodAndDateRange(
            PaymentMethod method, LocalDateTime from, LocalDateTime to
    ) {
        List<VisitPayment> payments = patientRepository.findAllPaymentsByMethodAndDateRange(method, from, to);

        return payments.stream().map(vp -> {
            var visit = vp.getVisit();
            var patient = visit.getPatient();
            var doctor = visit.getDoctor();

            LocalDateTime visitDate = visit.getCheckInTime(); // fallback; or use appointment/walkin time if needed

            return new PatientPaymentDTO(
                    vp.getId(),
                    vp.getAmount(),
                    vp.getPaymentMethod(),
                    vp.getPaidAt(),

                    patient.getFirstName() + " " + patient.getLastName(),

                    doctor != null
                            ? doctor.getFirstName() + " " + doctor.getLastName()
                            : null,

                    visit.getVisitType(),
                    visitDate,

                    // Insurance fields
                    vp.getInsuranceProvider(),
                    vp.getInsuranceAmount(),
                    vp.getInsuranceClass(),
                    vp.getInsuranceType(),
                    vp.getCoveragePercent(),
                    vp.getInsuranceAcceptNumber(),
                    vp.getInsuranceCardId(),
                    vp.getInsuranceFormId(),
                    vp.getInsurancePaid(),
                    vp.getInsurancePaidAmount(),
                    vp.getInsuranceDiscount(),

                    // POS fields
                    vp.getTerminalId(),
                    vp.getReferenceNumber(),
                    vp.getCardType(),
                    vp.getApprovalCode()
            );
        }).collect(Collectors.toList());
    }



    // ---------------- CREATE ----------------

    /** Create TEMP patient */
    public Patient createTempPatient(Patient patient) {
        normalize(patient);
        patient.setPatientType(PatientType.TEMP);
        patient.setPassportNumber(null); // TEMP cannot have passport
        return patientRepository.save(patient);
    }

    /** Create PERMANENT patient */
    public Patient createPermanentPatient(Patient patient) {
        normalize(patient);
        if (patient.getPassportNumber() != null && patient.getPassportNumber().isBlank()) {
            patient.setPassportNumber(null);
        }
        patient.setPatientType(PatientType.PERMANENT);
        return patientRepository.save(patient);
    }

    /** Upgrade TEMP patient to PERMANENT */
    public Patient upgradeToPermanent(Long id, Patient fullData) {
        Patient existing = getPatientById(id);

        if (existing.getPatientType() == PatientType.PERMANENT) {
            throw new IllegalStateException("Patient is already permanent");
        }

        // Copy new fields from fullData
        existing.setFirstName(lower(fullData.getFirstName()));
        existing.setMiddleName(lower(fullData.getMiddleName()));
        existing.setLastName(lower(fullData.getLastName()));
        existing.setPhone(lower(fullData.getPhone()));
        existing.setAddress(fullData.getAddress());
        existing.setGender(fullData.getGender());
        existing.setDateOfBirth(fullData.getDateOfBirth());
        existing.setPassportNumber(
                fullData.getPassportNumber() != null && fullData.getPassportNumber().isBlank()
                        ? null
                        : fullData.getPassportNumber()
        );

        existing.setPatientType(PatientType.PERMANENT);
        return patientRepository.save(existing);
    }

    // ---------------- NORMALIZATION ----------------
    private void normalize(Patient p) {
        p.setFirstName(lower(p.getFirstName()));
        p.setMiddleName(lower(p.getMiddleName()));
        p.setLastName(lower(p.getLastName()));
        p.setPhone(lower(p.getPhone()));
    }

    private String lower(String v) {
        return v == null ? null : v.toLowerCase();
    }

    // ---------------- GET ----------------
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
    }

    // ---------------- SEARCH ----------------
//    public List<Patient> searchByMobile(String mobile) {
//        if (mobile != null) mobile = mobile.toLowerCase();
//        return patientRepository.findByPhoneContainingIgnoreCase(mobile);
//    }
    public List<Patient> searchByMobile(String mobile) {
        System.out.println("Service received: [" + mobile + "]");

        if (mobile != null) {
            mobile = mobile.replace("+", "").trim();
        }

        return patientRepository.findByPhoneContainingIgnoreCase(mobile);
    }
//    public List<Patient> searchByFirstOrLastName(String name) {
//        if (name != null) name = name.toLowerCase();
//        return patientRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(name, name);
//    }

    public List<Patient> searchByFirstOrLastName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }

        name = name.trim();

        // If contains space, use the full name search
        if (name.contains(" ")) {
            return patientRepository.searchByFullName(name);
        } else {
            // Single word search
            return patientRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
                    name, name
            );
        }
    }

    // ---------------- UPDATE ----------------
//    public Patient updatePatient(Long id, Patient updated) {
//        Patient existing = getPatientById(id);
//
//        existing.setFirstName(lower(updated.getFirstName()));
//        existing.setMiddleName(updated.getMiddleName() != null ? lower(updated.getMiddleName()) : "");
//        existing.setLastName(lower(updated.getLastName()));
//        existing.setPhone(lower(updated.getPhone()));
//        existing.setAddress(updated.getAddress());
//        existing.setGender(updated.getGender());
//        existing.setDateOfBirth(updated.getDateOfBirth());
//
//        return patientRepository.save(existing);
//    }

    // ---------------- UPDATE ----------------
    public Patient updatePatient(Long id, Patient updated) {

        Patient existing = getPatientById(id);

        // -------- BASIC INFO --------
//        existing.setFirstName(lower(updated.getFirstName()));
        existing.setFirstName(updated.getFirstName());

        existing.setMiddleName(updated.getMiddleName() != null ? lower(updated.getMiddleName()) : "");
//        existing.setLastName(lower(updated.getLastName()));
        existing.setLastName(updated.getLastName());


        existing.setPhone(lower(updated.getPhone()));
        existing.setAddress(updated.getAddress());
        existing.setGender(updated.getGender());
        existing.setDateOfBirth(updated.getDateOfBirth());

        existing.setCountry(updated.getCountry());
        existing.setCity(updated.getCity());
        existing.setInsuranceProvider(updated.getInsuranceProvider());

        // -------- PASSPORT NUMBER (NEW) --------
        existing.setPassportNumber(
                updated.getPassportNumber() != null && updated.getPassportNumber().isBlank()
                        ? null
                        : updated.getPassportNumber()
        );

        // -------- INSURANCE CLASS (FIX) --------
        boolean classA = updated.isClassA();
        boolean classB = updated.isClassB();
        boolean classC = updated.isClassC();

        if (classA) {
            existing.setClassA(true);
            existing.setClassB(false);
            existing.setClassC(false);
        } else if (classB) {
            existing.setClassA(false);
            existing.setClassB(true);
            existing.setClassC(false);
        } else if (classC) {
            existing.setClassA(false);
            existing.setClassB(false);
            existing.setClassC(true);
        } else {
            existing.setClassA(false);
            existing.setClassB(false);
            existing.setClassC(false);
        }


        // ❌ NEVER change patientType here

        return patientRepository.save(existing);
    }


    // ---------------- DELETE ----------------
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete — patient not found with ID " + id);
        }
        patientRepository.deleteById(id);
    }


}
