package com.clinic.doctor_app_backend.repository;

import com.clinic.doctor_app_backend.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

}