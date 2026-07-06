package com.clinic.doctor_app_backend.repository;


import com.clinic.doctor_app_backend.model.VisitDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitDrugRepository extends JpaRepository<VisitDrug, Long> {


    Optional<VisitDrug> findByVisit_IdAndDrug_DrugId(Long visitId, Long drugId);
}