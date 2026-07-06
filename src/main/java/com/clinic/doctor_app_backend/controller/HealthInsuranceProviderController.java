package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.model.HealthInsuranceProvider;
import com.clinic.doctor_app_backend.service.HealthInsuranceProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@CrossOrigin("*")

public class HealthInsuranceProviderController {

    private final HealthInsuranceProviderService service;

    public HealthInsuranceProviderController(HealthInsuranceProviderService service) {
        this.service = service;
    }

    // Get providers by country
    @GetMapping("/country/{country}")
    public List<HealthInsuranceProvider> getProvidersByCountry(@PathVariable("country") String country) {
        return service.getProvidersByCountry(country);
    }

    // Get all providers
    @GetMapping
    public List<HealthInsuranceProvider> getAllProviders() {
        return service.getAllProviders();
    }

    // Add a new provider
    @PostMapping
    public HealthInsuranceProvider addProvider(@RequestBody HealthInsuranceProvider provider) {
        return service.saveProvider(provider);
    }


    // Delete a provider by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        boolean deleted = service.deleteProvider(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Delete all providers by country
    @DeleteMapping("/country/{country}")
    public ResponseEntity<Void> deleteProvidersByCountry(@PathVariable("country") String country) {
        boolean deleted = service.deleteProvidersByCountry(country);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}

