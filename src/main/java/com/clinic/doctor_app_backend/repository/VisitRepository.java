package com.clinic.doctor_app_backend.repository;


import com.clinic.doctor_app_backend.choices.PaymentMethod;
import com.clinic.doctor_app_backend.model.Drug;
import com.clinic.doctor_app_backend.model.WalkInModel;
import com.clinic.doctor_app_backend.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface VisitRepository extends JpaRepository<Visit, Long> {


    List<Visit> findAllByDoctor_Id(Long doctorId);

    Optional<Visit> findByAppointmentId(Long appointmentId);

    // Find visits between two dates
    Optional<Visit> findByWalkInId(Long walkInId);

    // Check if visit exists for a walk-in
    boolean existsByWalkInId(Long walkInId);

    // Find all visits that have a walk-in associated
    List<Visit> findAllByWalkInIsNotNull();

    // Find all visits without walk-in association
    List<Visit> findAllByWalkInIsNull();

    List<Visit> findByPatientId(Long patientId);



    @Query("""
SELECT v FROM Visit v
LEFT JOIN v.appointment a
LEFT JOIN v.walkIn w
WHERE v.doctor.id = :doctorId
ORDER BY COALESCE(a.appointmentTime, w.visitTime, v.checkInTime) DESC
""")
    Page<Visit> findVisitsByDoctorOrdered(
            @Param("doctorId") Long doctorId,
            Pageable pageable
    );
//    Page<Visit> findByDoctorId(Long doctorId, Pageable pageable);

    @Query("""
    SELECT v.consultationStart, v.consultationEnd
    FROM Visit v
    WHERE v.doctor.id = :doctorId
      AND FUNCTION('DATE', v.consultationStart) = :date
""")
    List<Object[]> findVisitsByDoctorAndDate(@Param("doctorId") Long doctorId,
                                             @Param("date") LocalDate date);


    @Query("SELECT v FROM WalkInModel v WHERE v.room.id = :roomId")
    List<WalkInModel> findByRoomId(@Param("roomId") Long roomId);

//new for payment
//
//    List<Visit> findByPaidFalse();
//
//    List<Visit> findByPaidTrue();
////    List<Visit> findByPaymentMethod(PaymentMethod paymentMethod);
//    List<Visit> findByDoctorIdAndPaidFalse(Long doctorId);
//    List<Visit> findByPatientIdAndPaidTrue(Long patientId);
//    List<Visit> findByInsuranceProviderIsNotNull(); // insurance visits
//    List<Visit> findByInsuranceProviderIsNull();    // cash visits
//    @Query("""
//  SELECT v FROM Visit v
//  WHERE v.paid = true
//    AND FUNCTION('DATE', v.updatedAt) = :date
//""")
//    List<Visit> findPaidVisitsByDate(@Param("date") LocalDate date);
//
//
//@Query("SELECT v.drugs FROM Visit v WHERE v.id = :visitId")
//List<Drug> findDrugsByVisitId(@Param("visitId") Long visitId);

@Query("""
    SELECT vd.drug
    FROM VisitDrug vd
    WHERE vd.visit.id = :visitId
""")
List<Drug> findDrugsByVisitId(@Param("visitId") Long visitId);


    List<Visit> findByPatientIdIn(List<Long> patientIds);



    @Query("""
    SELECT v FROM Visit v
    WHERE v.doctor.id = :doctorId
    AND LOWER(CONCAT(v.patient.firstName, ' ', v.patient.lastName))
        LIKE LOWER(CONCAT('%', :name, '%'))
    ORDER BY v.id DESC
""")
    List<Visit> searchByDoctorAndPatientName(
            @Param("doctorId") Long doctorId,
            @Param("name") String name
    );


    @Query("""
SELECT COUNT(v)
FROM Visit v
WHERE v.doctor.id = :doctorId
AND v.id > :visitId
""")
    long countVisitsBefore(
            @Param("doctorId") Long doctorId,
            @Param("visitId") Long visitId
    );



}
