package com.clinic.doctor_app_backend.service;


import com.clinic.doctor_app_backend.choices.AppointmentStatus;
import com.clinic.doctor_app_backend.choices.VisitStatus;
import com.clinic.doctor_app_backend.dto.PatientScheduleDTO;
import com.clinic.doctor_app_backend.model.Appointment;
import com.clinic.doctor_app_backend.model.WalkInModel;
import com.clinic.doctor_app_backend.repository.AppointmentRepository;
import com.clinic.doctor_app_backend.repository.WalkInRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final AppointmentRepository appointmentRepository;
    private final WalkInRepository walkInRepository;

    public ScheduleService(AppointmentRepository appointmentRepository,
                           WalkInRepository visitRepository) {
        this.appointmentRepository = appointmentRepository;
        this.walkInRepository = visitRepository;
    }

    public PatientScheduleDTO getPatientSchedule(Long patientId) {

        // Get appointments excluding CLOSED or CANCELLED
        List<PatientScheduleDTO.AppointmentDTO> appointments = appointmentRepository.findByPatientId(patientId)
                .stream()
                .filter(a -> a.getStatus() != AppointmentStatus.CLOSED &&
                        a.getStatus() != AppointmentStatus.CANCELLED &&
                        a.getStatus() != AppointmentStatus.IN_PROGRESS &&
                        a.getStatus() != AppointmentStatus.NEW
                )
                .map(a -> new PatientScheduleDTO.AppointmentDTO(
                        a.getId(),
                        a.getAppointmentTime(),
                        a.getDoctorName(),
                        a.getRoom() != null ? a.getRoom().getRoomNumber() : null
                ))
                .collect(Collectors.toList());
//        // Get appointments
//        List<PatientScheduleDTO.AppointmentDTO> appointments = appointmentRepository.findByPatientId(patientId)
//                .stream()
//                .map(a -> new PatientScheduleDTO.AppointmentDTO(
//                        a.getId(),
//                        a.getAppointmentTime(),
//                        a.getDoctorName(),
//                        a.getRoom() != null ? a.getRoom().getRoomNumber() : null
//                ))
//                .collect(Collectors.toList());

//        // Get visits
//        List<PatientScheduleDTO.VisitDTO> visits = visitRepository.findByPatientId(patientId)
//                .stream()
//                .map(v -> new PatientScheduleDTO.VisitDTO(
//                        v.getId(),
//                        v.getVisitTime(),
//                        v.getDoctorName(),
//                        v.getRoom() != null ? v.getRoom().getRoomNumber() : null
//                ))
//                .collect(Collectors.toList());

//        return new PatientScheduleDTO(patientId, appointments, visits);

// Get visits excluding CLOSED or CANCELLED


        List<PatientScheduleDTO.VisitDTO> visits = walkInRepository.findByPatientId(patientId)
                .stream()
                .filter(v -> v.getStatus() != VisitStatus.CLOSED &&
                        v.getStatus() != VisitStatus.CANCELLED&&
                        v.getStatus() != VisitStatus.IN_PROGRESS)
                .map(v -> new PatientScheduleDTO.VisitDTO(
                        v.getId(),
                        v.getVisitTime(),
                        v.getDoctorName(),
                        v.getRoom() != null ? v.getRoom().getRoomNumber() : null
                ))
                .collect(Collectors.toList());

        return new PatientScheduleDTO(patientId, appointments, visits);
    }

    public List<Appointment> getAppointmentsForDate(LocalDate date) {
        return appointmentRepository.findAppointmentsByDate(date);
    }

    public List<WalkInModel> getWalkInsForDate(LocalDate date) {
        return walkInRepository.findWalkInsByDate(date);
    }
}

