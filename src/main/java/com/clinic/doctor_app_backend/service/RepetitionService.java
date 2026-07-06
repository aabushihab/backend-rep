package com.clinic.doctor_app_backend.service;


import com.clinic.doctor_app_backend.model.Repetition;
import com.clinic.doctor_app_backend.repository.RepetitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepetitionService {

    private final RepetitionRepository repetitionRepository;

    public RepetitionService(RepetitionRepository repetitionRepository) {
        this.repetitionRepository = repetitionRepository;
    }

    public List<Repetition> getAllRepetitions() {
        return repetitionRepository.findAll();
    }
}