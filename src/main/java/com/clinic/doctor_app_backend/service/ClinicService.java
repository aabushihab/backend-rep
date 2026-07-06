package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.model.Clinic;
import com.clinic.doctor_app_backend.repository.ClinicRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    // Add clinic with default language "en"
    public Clinic createClinic(String name) {
        Clinic clinic = new Clinic();
        clinic.setName(name);
        clinic.setLanguage("en"); // default language
        return clinicRepository.save(clinic);
    }

    // Update clinic name
    public Clinic updateClinicName(Long id, String name) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        clinic.setName(name);
        return clinicRepository.save(clinic);
    }

    // Update clinic language
    public Clinic updateClinicLanguage(Long id, String language) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        clinic.setLanguage(language);
        return clinicRepository.save(clinic);
    }


    // Get clinic info + DATE/TIME NOW
    public Object getClinicInfo(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        LocalDateTime now = LocalDateTime.now();

        return new Object() {
            public final Long clinicId = clinic.getId();
            public final String clinicName = clinic.getName();
            public final String language = clinic.getLanguage(); // include language
            public final String date = now.toLocalDate().toString();
            public final String time = now.toLocalTime().withSecond(0).toString();
            public final String day =
                    now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        };
    }
}
