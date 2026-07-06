package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.model.Clinic;
import com.clinic.doctor_app_backend.service.ClinicService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clinic")
@CrossOrigin("*")
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    // 3️⃣ Get clinic name + DATE/TIME NOW
    @GetMapping("/{id}/info")
    public Object getClinicInfo(@PathVariable("id") Long id) {
        return clinicService.getClinicInfo(id);
    }

    // Update clinic language
    @PutMapping("/{id}/language")
    public Clinic updateLanguage(@PathVariable("id") Long id, @RequestParam (name = "language") String language) {
        // Accept "en" or "ar" (you can validate if needed)
        return clinicService.updateClinicLanguage(id, language);
    }
}
