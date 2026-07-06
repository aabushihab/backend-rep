package com.clinic.doctor_app_backend.repository;


import com.clinic.doctor_app_backend.model.Appointment;
import com.clinic.doctor_app_backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();
    @Query("SELECT a FROM Appointment a WHERE a.room.id = :roomId")
    List<Appointment> findByRoomId(@Param("roomId") Long roomId);
    // Get all appointments for a patient
    List<Appointment> findByPatient(Patient patient);

    List<Appointment> findByDoctorId(Long doctorId);
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentTime = :time")
    List<Appointment> findByDoctorAndTime(@Param("doctorId") Long doctorId,
                                          @Param("time") LocalDateTime time);

    // Last month appointments per doctor
    @Query("""
       SELECT CONCAT(a.doctor.firstName, ' ', a.doctor.lastName), COUNT(a)
       FROM Appointment a
       WHERE a.appointmentTime >= :startDate
         AND a.appointmentTime < :endDate
       GROUP BY a.doctor.firstName, a.doctor.lastName
       """)
    List<Object[]> countAppointmentsLastMonth(LocalDateTime startDate, LocalDateTime endDate);


    //Appointment per doctor
    @Query("""
       SELECT CONCAT(a.doctor.firstName, ' ', a.doctor.lastName), COUNT(a)
       FROM Appointment a
       GROUP BY a.doctor.firstName, a.doctor.lastName
       """)
    List<Object[]> countAppointmentsPerDoctor();

//    List<Appointment> findByPatientIdAndDate(Long patientId, LocalDate date);


    @Query("""
    SELECT a.appointmentTime, a.appointmentTime + :duration
    FROM Appointment a
    WHERE a.doctor.id = :doctorId
      AND FUNCTION('DATE', a.appointmentTime) = :date
""")
    List<Object[]> findAppointmentsByDoctorAndDate(@Param("doctorId") Long doctorId,
                                                   @Param("date") LocalDate date,
                                                   @Param("duration") Duration duration);


    @Query("SELECT a FROM Appointment a WHERE a.room.id = :roomId AND a.appointmentTime = :time")
    List<Appointment> findByRoomAndTime(@Param("roomId") Long roomId,
                                        @Param("time") LocalDateTime time);

    List<Appointment> findByPatientId(Long patientId);


    @Query("SELECT a FROM Appointment a WHERE a.room.id = :roomId AND a.status <> 'CLOSED'")
    List<Appointment> findByRoomIdAndStatusNotClosed(@Param("roomId") Long roomId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status <> 'CLOSED'")
    List<Appointment> findByDoctorIdAndStatusNotClosed(@Param("doctorId") Long doctorId);
    // Fetch appointments for a specific date
    @Query("SELECT a FROM Appointment a WHERE FUNCTION('DATE', a.appointmentTime) = :date ORDER BY a.id ASC")
    List<Appointment> findAppointmentsByDate(@Param("date") LocalDate date);


    @Query("""
SELECT a FROM Appointment a
WHERE a.appointmentTime BETWEEN :start AND :end
AND (:doctorId IS NULL OR a.doctor.id = :doctorId)
AND (:roomId IS NULL OR a.room.id = :roomId)
AND a.status <> 'CANCELLED'
""")
    List<Appointment> findConflicts(
            @Param("doctorId") Long doctorId,
            @Param("roomId") Long roomId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
//package com.clinic.doctor_app_backend.repository;
//
//import com.clinic.doctor_app_backend.model.Appointment;
//import com.clinic.doctor_app_backend.model.Patient;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
//
//    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
//    List<Object[]> countAppointmentsByStatus();
//
//    @Query("SELECT a FROM Appointment a WHERE a.room.id = :roomId")
//    List<Appointment> findByRoomId(@Param("roomId") Long roomId);
//
//    List<Appointment> findByPatient(Patient patient);
//
//    List<Appointment> findByDoctorId(Long doctorId);
//
//    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentTime = :time")
//    List<Appointment> findByDoctorAndTime(@Param("doctorId") Long doctorId,
//                                          @Param("time") LocalDateTime time);
//
//    @Query("SELECT CONCAT(a.doctor.firstName, ' ', a.doctor.lastName), COUNT(a) " +
//            "FROM Appointment a " +
//            "WHERE a.appointmentTime >= :startDate AND a.appointmentTime < :endDate " +
//            "GROUP BY a.doctor.firstName, a.doctor.lastName")
//    List<Object[]> countAppointmentsLastMonth(@Param("startDate") LocalDateTime startDate,
//                                              @Param("endDate") LocalDateTime endDate);
//
//    @Query("SELECT CONCAT(a.doctor.firstName, ' ', a.doctor.lastName), COUNT(a) " +
//            "FROM Appointment a " +
//            "GROUP BY a.doctor.firstName, a.doctor.lastName")
//    List<Object[]> countAppointmentsPerDoctor();
//
//    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
//            "AND FUNCTION('DATE', a.appointmentTime) = :date")
//    List<Appointment> findAppointmentsByDoctorAndDate(@Param("doctorId") Long doctorId,
//                                                      @Param("date") LocalDate date);
//
//    @Query("SELECT a FROM Appointment a WHERE a.room.id = :roomId AND a.appointmentTime = :time")
//    List<Appointment> findByRoomAndTime(@Param("roomId") Long roomId,
//                                        @Param("time") LocalDateTime time);
//
//    List<Appointment> findByPatientId(Long patientId);
//
//    @Query("SELECT a FROM Appointment a WHERE a.room.id = :roomId AND a.status <> 'CLOSED'")
//    List<Appointment> findByRoomIdAndStatusNotClosed(@Param("roomId") Long roomId);
//
//    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status <> 'CLOSED'")
//    List<Appointment> findByDoctorIdAndStatusNotClosed(@Param("doctorId") Long doctorId);
//
//    @Query("SELECT a FROM Appointment a WHERE FUNCTION('DATE', a.appointmentTime) = :date ORDER BY a.id ASC")
//    List<Appointment> findAppointmentsByDate(@Param("date") LocalDate date);
//}

