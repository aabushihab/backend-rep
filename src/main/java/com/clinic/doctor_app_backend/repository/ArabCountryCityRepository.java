package com.clinic.doctor_app_backend.repository;


import com.clinic.doctor_app_backend.model.ArabCountryCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArabCountryCityRepository extends JpaRepository<ArabCountryCity, Long> {
    List<ArabCountryCity> findByCountryName(String countryName);
    boolean existsByCountryNameAndCityName(String countryName, String cityName);

}

