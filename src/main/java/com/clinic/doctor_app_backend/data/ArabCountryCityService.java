package com.clinic.doctor_app_backend.data;

import com.clinic.doctor_app_backend.model.ArabCountryCity;
import com.clinic.doctor_app_backend.repository.ArabCountryCityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArabCountryCityService {

    private final ArabCountryCityRepository repository;

    public ArabCountryCityService(ArabCountryCityRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void insertAllCities() {

        // ✅ Prevent inserting again if data already exists
        if (repository.count() > 0) {
            System.out.println("Arab countries and cities already exist. Skipping insert.");
            return;
        }

        List<ArabCountryCity> cities = List.of(
                // Jordan
                new ArabCountryCity("Jordan", "+962", "Amman"),
                new ArabCountryCity("Jordan", "+962", "Zarqa"),
                new ArabCountryCity("Jordan", "+962", "Irbid"),
                new ArabCountryCity("Jordan", "+962", "Aqaba"),
                new ArabCountryCity("Jordan", "+962", "Karak"),
                new ArabCountryCity("Jordan", "+962", "Jerash"),
                new ArabCountryCity("Jordan", "+962", "As-Salt"),
                new ArabCountryCity("Jordan", "+962", "Madaba"),
                new ArabCountryCity("Jordan", "+962", "Ma’an"),
                new ArabCountryCity("Jordan", "+962", "Tafilah"),

                // Iraq
                new ArabCountryCity("Iraq", "+964", "Baghdad"),
                new ArabCountryCity("Iraq", "+964", "Basra"),
                new ArabCountryCity("Iraq", "+964", "Mosul"),
                new ArabCountryCity("Iraq", "+964", "Erbil"),
                new ArabCountryCity("Iraq", "+964", "Karbala"),
                new ArabCountryCity("Iraq", "+964", "Najaf"),
                new ArabCountryCity("Iraq", "+964", "Nasiriyah"),

                // Saudi Arabia
                new ArabCountryCity("Saudi Arabia", "+966", "Riyadh"),
                new ArabCountryCity("Saudi Arabia", "+966", "Jeddah"),
                new ArabCountryCity("Saudi Arabia", "+966", "Mecca"),
                new ArabCountryCity("Saudi Arabia", "+966", "Medina"),
                new ArabCountryCity("Saudi Arabia", "+966", "Dammam"),
                new ArabCountryCity("Saudi Arabia", "+966", "Taif"),
                new ArabCountryCity("Saudi Arabia", "+966", "Abha"),
                new ArabCountryCity("Saudi Arabia", "+966", "Al Khobar"),

                // Kuwait
                new ArabCountryCity("Kuwait", "+965", "Kuwait City"),
                new ArabCountryCity("Kuwait", "+965", "Al Jahra"),
                new ArabCountryCity("Kuwait", "+965", "Al Ahmadi"),
                new ArabCountryCity("Kuwait", "+965", "Hawalli"),
                new ArabCountryCity("Kuwait", "+965", "Al Farwaniyah"),

                // Bahrain
                new ArabCountryCity("Bahrain", "+973", "Manama"),
                new ArabCountryCity("Bahrain", "+973", "Muharraq"),
                new ArabCountryCity("Bahrain", "+973", "Riffa"),
                new ArabCountryCity("Bahrain", "+973", "Isa Town"),
                new ArabCountryCity("Bahrain", "+973", "Al-Riffa"),

                // Qatar
                new ArabCountryCity("Qatar", "+974", "Doha"),
                new ArabCountryCity("Qatar", "+974", "Al Rayyan"),
                new ArabCountryCity("Qatar", "+974", "Al Wakrah"),
                new ArabCountryCity("Qatar", "+974", "Umm Salal"),
                new ArabCountryCity("Qatar", "+974", "Al Khor"),

                // UAE
                new ArabCountryCity("United Arab Emirates", "+971", "Abu Dhabi"),
                new ArabCountryCity("United Arab Emirates", "+971", "Dubai"),
                new ArabCountryCity("United Arab Emirates", "+971", "Sharjah"),
                new ArabCountryCity("United Arab Emirates", "+971", "Ajman"),
                new ArabCountryCity("United Arab Emirates", "+971", "Fujairah"),
                new ArabCountryCity("United Arab Emirates", "+971", "Ras Al Khaimah"),
                new ArabCountryCity("United Arab Emirates", "+971", "Umm Al Quwain"),

                // Oman
                new ArabCountryCity("Oman", "+968", "Muscat"),
                new ArabCountryCity("Oman", "+968", "Salalah"),
                new ArabCountryCity("Oman", "+968", "Sohar"),
                new ArabCountryCity("Oman", "+968", "Nizwa"),
                new ArabCountryCity("Oman", "+968", "Bahla"),

                // Lebanon
                new ArabCountryCity("Lebanon", "+961", "Beirut"),
                new ArabCountryCity("Lebanon", "+961", "Tripoli"),
                new ArabCountryCity("Lebanon", "+961", "Sidon"),
                new ArabCountryCity("Lebanon", "+961", "Tyre"),
                new ArabCountryCity("Lebanon", "+961", "Zahle"),

                // Syria
                new ArabCountryCity("Syria", "+963", "Damascus"),
                new ArabCountryCity("Syria", "+963", "Aleppo"),
                new ArabCountryCity("Syria", "+963", "Homs"),
                new ArabCountryCity("Syria", "+963", "Hama"),
                new ArabCountryCity("Syria", "+963", "Latakia"),

                // Palestine
                new ArabCountryCity("Palestine", "+970", "Jerusalem"),
                new ArabCountryCity("Palestine", "+970", "Gaza City"),
                new ArabCountryCity("Palestine", "+970", "Ramallah"),
                new ArabCountryCity("Palestine", "+970", "Nablus"),
                new ArabCountryCity("Palestine", "+970", "Hebron")
        );

        repository.saveAll(cities);
        System.out.println("All Arab countries and major cities inserted successfully!");
    }
}
