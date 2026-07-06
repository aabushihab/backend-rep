package com.clinic.doctor_app_backend.dto;

import java.util.List;

public class CountryCitiesDTO {

    private String countryName;
    private String phoneCode;
    private List<String> cities;

    public CountryCitiesDTO(String countryName, String phoneCode, List<String> cities) {
        this.countryName = countryName;
        this.phoneCode = phoneCode;
        this.cities = cities;
    }

    public String getCountryName() { return countryName; }
    public String getPhoneCode() { return phoneCode; }
    public List<String> getCities() { return cities; }
}
