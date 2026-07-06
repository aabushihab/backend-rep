package com.clinic.doctor_app_backend.repository;
import com.clinic.doctor_app_backend.dto.ProcedureNameDto;
import com.clinic.doctor_app_backend.model.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, String> {
    List<Procedure> findByCategory(String category);

    List<Procedure> findByCategoryIgnoreCase(String category);


//    // ✅ FUZZY SEARCH WITH CATEGORY
//    @Query("""
//        SELECT p FROM Procedure p
//        WHERE LOWER(p.category) = LOWER(:category)
//        AND LOWER(p.procedureName) LIKE LOWER(CONCAT('%', :name, '%'))
//    """)
//    List<Procedure> searchByCategoryAndName(
//            @Param("category") String category,
//            @Param("name") String name
//    );


    @Query("""
    SELECT new com.clinic.doctor_app_backend.dto.ProcedureNameDto(p.procedureName)
    FROM Procedure p
    WHERE LOWER(p.category) = LOWER(:category)
    AND LOWER(p.procedureName) LIKE LOWER(CONCAT('%', :name, '%'))
""")
    List<ProcedureNameDto> searchByCategoryAndName(
            @Param("category") String category,
            @Param("name") String name
    );
}


