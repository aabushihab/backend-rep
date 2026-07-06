package com.clinic.doctor_app_backend.dto;

public class CountryDTO {
    private String countryName;

    public CountryDTO(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }
}
