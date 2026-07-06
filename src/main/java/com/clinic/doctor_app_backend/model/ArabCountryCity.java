package com.clinic.doctor_app_backend.model;

import jakarta.persistence.*;
@Entity
@Table(
        name = "arab_countries_cities",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_country_city",
                        columnNames = {"country_name", "city_name"}
                )
        }

)
public class ArabCountryCity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "phone_code", nullable = false)
    private String phoneCode;

    @Column(name = "city_name", nullable = false)
    private String cityName;


    // Constructors
    public ArabCountryCity() {}

    public ArabCountryCity(String countryName, String phoneCode, String cityName) {
        this.countryName = countryName;
        this.phoneCode = phoneCode;
        this.cityName = cityName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
    public String getPhoneCode() { return phoneCode; }
    public void setPhoneCode(String phoneCode) { this.phoneCode = phoneCode; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
}
