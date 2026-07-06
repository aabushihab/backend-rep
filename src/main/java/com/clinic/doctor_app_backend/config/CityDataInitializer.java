package com.clinic.doctor_app_backend.config;

import com.clinic.doctor_app_backend.data.ArabCountryCityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CityDataInitializer implements CommandLineRunner {

    private final ArabCountryCityService service;

    public CityDataInitializer(ArabCountryCityService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        service.insertAllCities();
    }
}
