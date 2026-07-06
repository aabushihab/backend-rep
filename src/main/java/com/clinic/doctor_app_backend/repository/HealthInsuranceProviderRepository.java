package com.clinic.doctor_app_backend.repository;


import com.clinic.doctor_app_backend.model.HealthInsuranceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthInsuranceProviderRepository extends JpaRepository<HealthInsuranceProvider, Long> {

    List<HealthInsuranceProvider> findByCountryIgnoreCase(String country);
    boolean existsByCountryAndName(String country, String name);
    void deleteByCountryIgnoreCase(String country);



}
