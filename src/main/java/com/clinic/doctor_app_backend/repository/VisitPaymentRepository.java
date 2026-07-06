package com.clinic.doctor_app_backend.repository;

import com.clinic.doctor_app_backend.dto.DoctorPaymentSummaryDTO;
import com.clinic.doctor_app_backend.model.VisitPayment;
import com.clinic.doctor_app_backend.choices.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitPaymentRepository extends JpaRepository<VisitPayment, Long> {

    // ================= CLINIC SUMMARY / TOTAL =================
//    @Query("""
//    SELECT
//        SUM(CASE WHEN vp.paymentMethod = :cash THEN vp.amount ELSE 0 END) as cash,
//        SUM(CASE WHEN vp.paymentMethod = :pos THEN vp.amount ELSE 0 END) as pos,
//        SUM(CASE WHEN vp.paymentMethod = :insurance THEN vp.insuranceAmount ELSE 0 END) as insurance,
//        SUM(CASE WHEN vp.paymentMethod = :free THEN 1 ELSE 0 END) as freeVisits
//    FROM VisitPayment vp
//    WHERE vp.paidAt BETWEEN :from AND :to
//""")
//    Object clinicSummaryByDate(
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to,
//            @Param("cash") PaymentMethod cash,
//            @Param("pos") PaymentMethod pos,
//            @Param("insurance") PaymentMethod insurance,
//            @Param("free") PaymentMethod free
//    );

    @Query("""
SELECT
    SUM(CASE WHEN vp.paymentMethod = :cash THEN vp.amount ELSE 0 END),
    SUM(CASE WHEN vp.paymentMethod = :pos THEN vp.amount ELSE 0 END),
    SUM(CASE WHEN vp.paymentMethod = :insurance THEN vp.insuranceAmount ELSE 0 END),
    SUM(CASE WHEN vp.paymentMethod = :insurance THEN COALESCE(vp.insuranceDiscount,0) ELSE 0 END),
    SUM(CASE WHEN vp.paymentMethod = :free THEN 1 ELSE 0 END)
FROM VisitPayment vp
WHERE vp.paidAt BETWEEN :from AND :to
""")
    Object clinicSummaryByDate(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("cash") PaymentMethod cash,
            @Param("pos") PaymentMethod pos,
            @Param("insurance") PaymentMethod insurance,
            @Param("free") PaymentMethod free
    );




    // ================= PER DOCTOR =================
    @Query("""
    SELECT d.id,
           CONCAT(d.firstName,
                  CASE WHEN d.middleName IS NOT NULL AND d.middleName <> '' THEN CONCAT(' ', d.middleName) ELSE '' END,
                  ' ',
                  d.lastName),
           SUM(CASE WHEN vp.paymentMethod = 'CASH' THEN vp.amount ELSE 0 END),
           SUM(CASE WHEN vp.paymentMethod = 'POS' THEN vp.amount ELSE 0 END),
SUM(CASE WHEN vp.paymentMethod = 'INSURANCE' THEN vp.insuranceAmount ELSE 0 END),
SUM(CASE WHEN vp.paymentMethod = 'INSURANCE' THEN COALESCE(vp.insuranceDiscount,0) ELSE 0 END),
           SUM(CASE WHEN vp.paymentMethod = 'FREE' THEN 1 ELSE 0 END)
    FROM VisitPayment vp
    JOIN vp.visit v
    JOIN v.doctor d
    WHERE vp.paidAt BETWEEN :from AND :to
    GROUP BY d.id, d.firstName, d.middleName, d.lastName
""")
    List<Object[]> doctorSummaryByDate(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );



    Optional<VisitPayment> findByInsuranceFormId(String insuranceFormId);

    Optional<VisitPayment> findByInsuranceCardId(String insuranceCardId);
//
//    List<VisitPayment> findByInsuranceFormIdOrInsuranceCardId(String insuranceFormId, String insuranceCardId);
//// Optional: Case-insensitive search
@Query("SELECT vp FROM VisitPayment vp WHERE LOWER(vp.insuranceAcceptNumber) = LOWER(:acceptNumber)")
List<VisitPayment> searchByAcceptNumberIgnoreCase(@Param("acceptNumber") String acceptNumber);









    // All insurance payments
    @Query("""
            SELECT vp
            FROM VisitPayment vp
            WHERE vp.insuranceAmount IS NOT NULL
            ORDER BY vp.paidAt DESC
            """)
    List<VisitPayment> findAllInsurancePayments();

    // Unpaid insurance claims
    @Query("""
            SELECT vp
            FROM VisitPayment vp
            WHERE vp.insurancePaid = false
            ORDER BY vp.paidAt DESC
            """)
    List<VisitPayment> findUnpaidInsuranceClaims();
//
//    // By insurance provider
//    @Query("""
//            SELECT vp
//            FROM VisitPayment vp
//            WHERE vp.insuranceProvider = :provider
//            ORDER BY vp.paidAt DESC
//            """)
//    List<VisitPayment> findByInsuranceProvider(
//            @Param("provider") String provider
//    );

    // By insurance provider (case-insensitive, contains match)
    @Query("""
        SELECT vp
        FROM VisitPayment vp
        WHERE LOWER(vp.insuranceProvider) LIKE LOWER(CONCAT('%', :provider, '%'))
        ORDER BY vp.paidAt DESC
        """)
    List<VisitPayment> findByInsuranceProvider(
            @Param("provider") String provider
    );
    // By acceptance number
    @Query("""
            SELECT vp
            FROM VisitPayment vp
            WHERE vp.insuranceAcceptNumber = :acceptNumber
            """)
    Optional<VisitPayment> findByInsuranceAcceptNumber(
            @Param("acceptNumber") String acceptNumber
    );

    // Outstanding claims
    @Query("""
            SELECT vp
            FROM VisitPayment vp
            WHERE vp.insuranceAmount IS NOT NULL
            AND (
                vp.insurancePaid = false
                OR COALESCE(vp.insurancePaidAmount,0) + COALESCE(vp.insuranceDiscount,0)
                   < COALESCE(vp.insuranceAmount,0)
            )
            ORDER BY vp.paidAt DESC
            """)
    List<VisitPayment> findOutstandingInsuranceClaims();

    // Provider summary
    @Query("""
            SELECT
                vp.insuranceProvider,
                COUNT(vp),
                SUM(COALESCE(vp.insuranceAmount,0)),
                SUM(COALESCE(vp.insurancePaidAmount,0))
            FROM VisitPayment vp
            WHERE vp.insuranceAmount IS NOT NULL
            GROUP BY vp.insuranceProvider
            ORDER BY vp.insuranceProvider
            """)
    List<Object[]> insuranceSummaryByProvider();

    // Insurance payments between dates
    @Query("""
            SELECT vp
            FROM VisitPayment vp
            WHERE vp.insuranceAmount IS NOT NULL
            AND vp.paidAt BETWEEN :fromDate AND :toDate
            ORDER BY vp.paidAt DESC
            """)
    List<VisitPayment> findInsurancePaymentsBetweenDates(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    List<VisitPayment> findByIdIn(List<Long> ids);

}


//package com.clinic.doctor_app_backend.repository;
//
//import com.clinic.doctor_app_backend.model.VisitPayment;
//import com.clinic.doctor_app_backend.choices.PaymentMethod;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//public interface VisitPaymentRepository extends JpaRepository<VisitPayment, Long> {
//
//    // ================= CLINIC SUMMARY / TOTAL =================
//    @Query(
//            "SELECT " +
//                    "SUM(CASE WHEN vp.paymentMethod = :cash THEN vp.amount ELSE 0 END) as cash, " +
//                    "SUM(CASE WHEN vp.paymentMethod = :pos THEN vp.amount ELSE 0 END) as pos, " +
//                    "SUM(CASE WHEN vp.paymentMethod = :insurance THEN vp.insuranceAmount ELSE 0 END) as insurance, " +
//                    "SUM(CASE WHEN vp.paymentMethod = :free THEN 1 ELSE 0 END) as freeVisits " +
//                    "FROM VisitPayment vp " +
//                    "WHERE vp.paidAt BETWEEN :from AND :to"
//    )
//    Object clinicSummaryByDate(
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to,
//            @Param("cash") PaymentMethod cash,
//            @Param("pos") PaymentMethod pos,
//            @Param("insurance") PaymentMethod insurance,
//            @Param("free") PaymentMethod free
//    );
//
//    // ================= PER DOCTOR =================
//    @Query(
//            "SELECT d.id, " +
//                    "CONCAT(d.firstName, " +
//                    "       CASE WHEN d.middleName IS NOT NULL AND d.middleName <> '' THEN CONCAT(' ', d.middleName) ELSE '' END, " +
//                    "       ' ', " +
//                    "       d.lastName), " +
//                    "SUM(CASE WHEN vp.paymentMethod = 'CASH' THEN vp.amount ELSE 0 END), " +
//                    "SUM(CASE WHEN vp.paymentMethod = 'POS' THEN vp.amount ELSE 0 END), " +
//                    "SUM(CASE WHEN vp.paymentMethod = 'INSURANCE' THEN vp.insuranceAmount ELSE 0 END), " +
//                    "SUM(CASE WHEN vp.paymentMethod = 'FREE' THEN 1 ELSE 0 END) " +
//                    "FROM VisitPayment vp " +
//                    "JOIN vp.visit v " +
//                    "JOIN v.doctor d " +
//                    "WHERE vp.paidAt BETWEEN :from AND :to " +
//                    "GROUP BY d.id, d.firstName, d.middleName, d.lastName"
//    )
//    List<Object[]> doctorSummaryByDate(
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to
//    );
//
//    Optional<VisitPayment> findByInsuranceFormId(String insuranceFormId);
//
//    Optional<VisitPayment> findByInsuranceCardId(String insuranceCardId);
//
//    List<VisitPayment> findByInsuranceFormIdOrInsuranceCardId(String insuranceFormId, String insuranceCardId);
//
//    // Optional: Case-insensitive search
//    @Query("SELECT vp FROM VisitPayment vp WHERE LOWER(vp.insuranceAcceptNumber) = LOWER(:acceptNumber)")
//    List<VisitPayment> searchByAcceptNumberIgnoreCase(@Param("acceptNumber") String acceptNumber);
//
//}
