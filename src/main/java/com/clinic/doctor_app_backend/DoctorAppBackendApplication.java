package com.clinic.doctor_app_backend;

import com.clinic.doctor_app_backend.data.ArabCountryCityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DoctorAppBackendApplication implements CommandLineRunner {
    private final ArabCountryCityService service;



    public DoctorAppBackendApplication(ArabCountryCityService service) {
        this.service = service;
    }

    public static void main(String[] args) {
		SpringApplication.run(DoctorAppBackendApplication.class, args);
	}


    @Override
    public void run(String... args) throws Exception {
        service.insertAllCities();

    }
}
