package com.clinic.doctor_app_backend.repository;

import com.clinic.doctor_app_backend.choices.PatientType;
import com.clinic.doctor_app_backend.choices.PaymentMethod;
import com.clinic.doctor_app_backend.model.Patient;
import com.clinic.doctor_app_backend.model.VisitPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {



//    Optional<Patient> findByFirstNameAndMiddleNameAndLastNameAndPhone(
//            String firstName, String middleName, String lastName, String phone
//    );

    List<Patient> findByPhoneContainingIgnoreCase(String phone);

    List<Patient> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
            String firstName, String lastName
    );

//    List<Patient> findByPatientType(PatientType patientType);

    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Patient> searchByFullName(@Param("searchTerm") String searchTerm);

    @Query("SELECT p.gender, COUNT(p) FROM Patient p GROUP BY p.gender")
    List<Object[]> countByGender();

//    @Query("SELECT p.address, COUNT(p) FROM Patient p GROUP BY p.address")
@Query("""
    SELECT p.city, COUNT(p)
    FROM Patient p
    GROUP BY p.city
    ORDER BY COUNT(p) DESC
""")
    List<Object[]> countByCity();


    @Query("""
    SELECT vp
    FROM VisitPayment vp
    JOIN FETCH vp.visit v
    JOIN FETCH v.patient p
    LEFT JOIN FETCH v.doctor d
    LEFT JOIN FETCH v.appointment a
    LEFT JOIN FETCH v.walkIn w
    WHERE vp.paymentMethod = :paymentMethod
      AND COALESCE(a.appointmentTime, w.visitTime, v.checkInTime)
          BETWEEN :fromDate AND :toDate
""")
    List<VisitPayment> findAllPaymentsByMethodAndDateRange(
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );





}


//
//package com.clinic.doctor_app_backend.repository;
//
//import com.clinic.doctor_app_backend.choices.PatientType;
//import com.clinic.doctor_app_backend.choices.PaymentMethod;
//import com.clinic.doctor_app_backend.model.Patient;
//import com.clinic.doctor_app_backend.model.VisitPayment;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface PatientRepository extends JpaRepository<Patient, Long> {
//
//    Optional<Patient> findByFirstNameAndMiddleNameAndLastNameAndPhone(
//            String firstName, String middleName, String lastName, String phone
//    );
//
//    List<Patient> findByPhoneContainingIgnoreCase(String phone);
//
//    List<Patient> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(
//            String firstName, String lastName
//    );
//
//    List<Patient> findByPatientType(PatientType patientType);
//
//    @Query("SELECT p.gender, COUNT(p) FROM Patient p GROUP BY p.gender")
//    List<Object[]> countByGender();
//
//    @Query("SELECT p.address, COUNT(p) FROM Patient p GROUP BY p.address")
//    List<Object[]> countByCity();
//
//    @Query(
//            "SELECT vp " +
//                    "FROM VisitPayment vp " +
//                    "JOIN FETCH vp.visit v " +
//                    "JOIN FETCH v.patient p " +
//                    "LEFT JOIN FETCH v.doctor d " +
//                    "LEFT JOIN FETCH v.appointment a " +
//                    "LEFT JOIN FETCH v.walkIn w " +
//                    "WHERE vp.paymentMethod = :paymentMethod " +
//                    "AND COALESCE(a.appointmentTime, w.visitTime, v.checkInTime) " +
//                    "BETWEEN :fromDate AND :toDate"
//    )
//    List<VisitPayment> findAllPaymentsByMethodAndDateRange(
//            @Param("paymentMethod") PaymentMethod paymentMethod,
//            @Param("fromDate") LocalDateTime fromDate,
//            @Param("toDate") LocalDateTime toDate
//    );
//
//}
