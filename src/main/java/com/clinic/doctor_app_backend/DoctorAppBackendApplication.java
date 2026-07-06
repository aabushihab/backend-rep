package com.clinic.doctor_app_backend;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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

    @Bean
    CommandLineRunner test(Environment env) {
        return args -> {
            System.out.println("PGHOST = " + env.getProperty("PGHOST"));
            System.out.println("PGPORT = " + env.getProperty("PGPORT"));
            System.out.println("PGDATABASE = " + env.getProperty("PGDATABASE"));
            System.out.println("PGUSER = " + env.getProperty("PGUSER"));
            System.out.println("spring.datasource.url = " + env.getProperty("spring.datasource.url"));
        };
}}
