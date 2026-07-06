package com.clinic.doctor_app_backend.repository;

import com.clinic.doctor_app_backend.model.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.clinic.doctor_app_backend.model.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long>,
        JpaSpecificationExecutor<Drug> {
    List<Drug> findByDrugIdIn(List<Long> ids);


    @Query("""
            SELECT d 
            FROM Drug d
            WHERE LOWER(d.tradeName) LIKE LOWER(CONCAT('%', :name, '%'))
               OR LOWER(d.genericName) LIKE LOWER(CONCAT('%', :name, '%'))
           """)
    List<Drug> autocomplete(@Param("name") String name);
}
