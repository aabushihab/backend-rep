package com.clinic.doctor_app_backend.repository;


import com.clinic.doctor_app_backend.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsById(Long id); // ✅ matches the 'id' field in Doctor

    @Query("SELECT d.specialty, COUNT(d) FROM Doctor d GROUP BY d.specialty")
    List<Object[]> countDoctorsBySpecialty();


    @Query("SELECT d FROM Doctor d WHERE TRIM(LOWER(d.username)) = LOWER(TRIM(:username))")
    Doctor findByUsername(@Param("username") String username);
Optional<Doctor> findByUsernameIgnoreCase(String username);

}

