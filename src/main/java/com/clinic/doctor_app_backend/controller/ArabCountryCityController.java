package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.dto.CountryCitiesDTO;
import com.clinic.doctor_app_backend.dto.CountryDTO;
import com.clinic.doctor_app_backend.model.ArabCountryCity;
import com.clinic.doctor_app_backend.repository.ArabCountryCityRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")

public class ArabCountryCityController {

    private final ArabCountryCityRepository repository;

    public ArabCountryCityController(ArabCountryCityRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/arab-cities/countries")
    public List<CountryDTO> getAllCountries() {
        List<ArabCountryCity> allCities = repository.findAll();

        // Use a Set to keep unique country names
        Set<String> countryNames = new LinkedHashSet<>();
        for (ArabCountryCity city : allCities) {
            countryNames.add(city.getCountryName());
        }

        // Convert to DTO list
        List<CountryDTO> countries = new ArrayList<>();
        for (String name : countryNames) {
            countries.add(new CountryDTO(name));
        }

        return countries;
    }


    @GetMapping("/api/arab-cities/full")
    public List<CountryCitiesDTO> getAllTableDetails() {
        List<ArabCountryCity> allCities = repository.findAll();

        // Group by country name and phone code
        Map<String, CountryCitiesDTO> map = new LinkedHashMap<>();
        for (ArabCountryCity city : allCities) {
            String key = city.getCountryName() + "|" + city.getPhoneCode();
            map.computeIfAbsent(key, k -> new CountryCitiesDTO(city.getCountryName(), city.getPhoneCode(), new ArrayList<>()))
                    .getCities().add(city.getCityName());
        }

        return new ArrayList<>(map.values());
    }
}
