package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.model.HealthInsuranceProvider;
import com.clinic.doctor_app_backend.repository.HealthInsuranceProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthInsuranceProviderService {

    private final HealthInsuranceProviderRepository repository;

    public HealthInsuranceProviderService(HealthInsuranceProviderRepository repository) {
        this.repository = repository;
    }

    public List<HealthInsuranceProvider> getProvidersByCountry(String country) {
        return repository.findByCountryIgnoreCase(country);
    }


    public List<HealthInsuranceProvider> getAllProviders() {
        return repository.findAll();
    }

    public HealthInsuranceProvider saveProvider(HealthInsuranceProvider provider) {
        return repository.save(provider);
    }


    @Transactional
    public boolean deleteProvider(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteProvidersByCountry(String country) {
        List<HealthInsuranceProvider> providers = repository.findByCountryIgnoreCase(country);
        if (!providers.isEmpty()) {
            repository.deleteAll(providers);
            return true;
        }
        return false;
    }

}
