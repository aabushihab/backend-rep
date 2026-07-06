package com.clinic.doctor_app_backend.repository;

import com.clinic.doctor_app_backend.model.Patient;
import com.clinic.doctor_app_backend.model.WalkInModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WalkInRepository extends JpaRepository<WalkInModel, Long> {

    List<WalkInModel> findByPatient(Patient patient);

    List<WalkInModel> findByDoctorId(Long doctorId);

    @Query("SELECT v FROM WalkInModel v WHERE v.doctor.id = :doctorId AND v.visitTime = :time")
    List<WalkInModel> findByDoctorAndTime(@Param("doctorId") Long doctorId,
                                          @Param("time") LocalDateTime time);

    @Query("SELECT v FROM WalkInModel v WHERE v.room.id = :roomId AND v.visitTime = :time")
    List<WalkInModel> findByRoomAndTime(@Param("roomId") Long roomId,
                                        @Param("time") LocalDateTime time);
    @Query("SELECT v FROM WalkInModel v WHERE v.room.id = :roomId")
    List<WalkInModel> findByRoomId(@Param("roomId") Long roomId);
    List<WalkInModel> findByPatientId(Long patientId);
    @Query("""
    SELECT v FROM WalkInModel v
    WHERE v.doctor.id = :doctorId
      AND v.visitTime = :time
      AND v.status <> com.clinic.doctor_app_backend.choices.VisitStatus.CLOSED
""")
    List<WalkInModel> findActiveByDoctorAndTime(@Param("doctorId") Long doctorId,
                                                @Param("time") LocalDateTime time);

    @Query("""
    SELECT v FROM WalkInModel v
    WHERE v.room.id = :roomId
      AND v.visitTime = :time
      AND v.status <> com.clinic.doctor_app_backend.choices.VisitStatus.CLOSED
""")
    List<WalkInModel> findActiveByRoomAndTime(@Param("roomId") Long roomId,
                                              @Param("time") LocalDateTime time);

    // Fetch walk-ins for a specific date

    @Query("SELECT w FROM WalkInModel w WHERE FUNCTION('DATE', w.visitTime) = :date ORDER BY w.id ASC")
    List<WalkInModel> findWalkInsByDate(@Param("date") LocalDate date);



    @Query("""
SELECT w FROM WalkInModel w
WHERE w.visitTime BETWEEN :start AND :end
AND (:doctorId IS NULL OR w.doctor.id = :doctorId)
AND (:roomId IS NULL OR w.room.id = :roomId)
AND w.status <> 'CANCELLED'
""")
    List<WalkInModel> findConflicts(
            @Param("doctorId") Long doctorId,
            @Param("roomId") Long roomId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


}
