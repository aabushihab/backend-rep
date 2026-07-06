package com.clinic.doctor_app_backend.service;


import com.clinic.doctor_app_backend.dto.DrugFilter;
import com.clinic.doctor_app_backend.model.Drug;
import com.clinic.doctor_app_backend.repository.DrugRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DrugService {

    private final DrugRepository drugRepository;

    public Page<Drug> search(DrugFilter filter, Pageable pageable) {

        return drugRepository.findAll(
                DrugSpecification.filter(filter),
                pageable
        );
    }

    public List<Drug> getDrugsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        return drugRepository.findByDrugIdIn(ids);
    }
    public List<Drug> autocomplete(String name) {

        return drugRepository.autocomplete(name)
                .stream()
                .limit(10)
                .toList();
    }
}