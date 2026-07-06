package com.clinic.doctor_app_backend.repository;


import com.clinic.doctor_app_backend.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {


}

