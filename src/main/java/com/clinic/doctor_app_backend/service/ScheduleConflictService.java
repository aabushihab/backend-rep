package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.choices.AppointmentStatus;
import com.clinic.doctor_app_backend.choices.VisitStatus;
import com.clinic.doctor_app_backend.model.Appointment;
import com.clinic.doctor_app_backend.model.WalkInModel;
import com.clinic.doctor_app_backend.repository.AppointmentRepository;
import com.clinic.doctor_app_backend.repository.WalkInRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleConflictService {

    @Value("${schedule.buffer.minutes:10}")
    private long bufferMinutes; // fallback = 10
    private final AppointmentRepository appointmentRepository;
    private final WalkInRepository visitRepository;

    public ScheduleConflictService(AppointmentRepository appointmentRepository,
                                   WalkInRepository visitRepository) {
        this.appointmentRepository = appointmentRepository;
        this.visitRepository = visitRepository;
    }

    public void checkDoctorAndRoomAvailability(
            Long doctorId,
            Long roomId,
            LocalDateTime time,
            Long ignoreId,
            boolean isVisit
    ) {

        final long BUFFER_MINUTES = 10;

        // ===================== CHECK DOCTOR =====================
        List<Appointment> doctorAppointments =
                appointmentRepository.findByDoctorIdAndStatusNotClosed(doctorId); // already excludes closed

        for (Appointment a : doctorAppointments) {
            if (ignoreId != null && a.getId().equals(ignoreId)) continue;

            // ✅ skip CLOSED appointments just in case
            if (a.getStatus() == AppointmentStatus.CLOSED) continue;

            long diff = Duration.between(a.getAppointmentTime(), time).toMinutes();
            if (Math.abs(diff) < bufferMinutes) {
                throw new RuntimeException("Doctor has another active appointment within 10 minutes");
            }
        }

        List<WalkInModel> doctorVisits = visitRepository.findByDoctorId(doctorId);

        for (WalkInModel v : doctorVisits) {
            if (ignoreId != null && v.getId().equals(ignoreId)) continue;

            // ✅ skip CLOSED visits
            if (v.getStatus().equals(AppointmentStatus.CLOSED) || v.getStatus() == VisitStatus.CLOSED) continue;

            long diff = Duration.between(v.getVisitTime(), time).toMinutes();
            if (Math.abs(diff) < bufferMinutes) {
                throw new RuntimeException("Doctor has another visit within 10 minutes");
            }
        }

        // ===================== CHECK ROOM =====================
        if (roomId != null) {

            List<Appointment> roomAppointments =
                    appointmentRepository.findByRoomIdAndStatusNotClosed(roomId);

            for (Appointment a : roomAppointments) {
                if (ignoreId != null && a.getId().equals(ignoreId)) continue;
                if (a.getStatus() == AppointmentStatus.CLOSED) continue;

                long diff = Duration.between(a.getAppointmentTime(), time).toMinutes();
                if (Math.abs(diff) < bufferMinutes) {
                    throw new RuntimeException("Room has another active appointment within 10 minutes");
                }
            }

            List<WalkInModel> roomVisits = visitRepository.findByRoomId(roomId);

            for (WalkInModel v : roomVisits) {
                if (ignoreId != null && v.getId().equals(ignoreId)) continue;
                if (v.getStatus().equals(AppointmentStatus.CLOSED) || v.getStatus() == VisitStatus.CLOSED) continue;

                long diff = Duration.between(v.getVisitTime(), time).toMinutes();
                if (Math.abs(diff) < bufferMinutes) {
                    throw new RuntimeException("Room has another visit within 10 minutes");
                }
            }
        }
    }

}
