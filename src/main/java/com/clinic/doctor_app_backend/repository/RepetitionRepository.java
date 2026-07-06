package com.clinic.doctor_app_backend.repository;

import com.clinic.doctor_app_backend.model.Repetition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepetitionRepository extends JpaRepository<Repetition, Long> {

    boolean existsByRepetitionCode(String repetitionCode);
}