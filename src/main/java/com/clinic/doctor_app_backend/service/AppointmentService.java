package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.choices.AppointmentStatus;
import com.clinic.doctor_app_backend.choices.VisitStatus;
import com.clinic.doctor_app_backend.model.*;
import com.clinic.doctor_app_backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final RoomRepository roomRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final LogService logService;
    private final VisitRepository visitRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              RoomRepository roomRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository, LogService logService, VisitRepository visitRepository) {
        this.appointmentRepository = appointmentRepository;
        this.roomRepository = roomRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.logService = logService;
        this.visitRepository = visitRepository;
    }




    // --------------------- SAVE APPOINTMENT ---------------------
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    // --------------------- GET APPOINTMENT BY ID ---------------------
    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    // --------------------- REASSIGN DOCTOR ---------------------
    public Appointment reassignDoctor(Long appointmentId, Long newDoctorId) {
        Appointment appointment = getAppointmentById(appointmentId);

        Doctor doctor = doctorRepository.findById(newDoctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Check doctor's schedule for conflicts
        List<Appointment> doctorAppointments = appointmentRepository.findByDoctorId(newDoctorId);

        for (Appointment existing : doctorAppointments) {
            // Skip the current appointment itself
            if (existing.getId().equals(appointmentId)) continue;

            long minutesDiff = Duration.between(existing.getAppointmentTime(), appointment.getAppointmentTime()).toMinutes();

            if (minutesDiff == 0) {
                throw new RuntimeException("Doctor already has an appointment at this time.");
            }
            if (Math.abs(minutesDiff) < 10) {
                throw new RuntimeException("Doctor must have at least 10 minutes between appointments.");
            }
        }

        appointment.setDoctor(doctor);
        return appointmentRepository.save(appointment);
    }


    // ------------------- CREATE APPOINTMENT -------------------
    public Appointment createAppointment(Long patientId, Long doctorId, Long roomId,
                                         LocalDateTime appointmentTime, String notes, AppointmentStatus status) {

        // --- Check patient ---
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // --- Check doctor ---
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // --- Check room ---
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // --- Check if room is already used at this time ---
        if (!appointmentRepository.findByRoomIdAndStatusNotClosed(roomId).stream()
                .filter(a -> a.getAppointmentTime().equals(appointmentTime))
                .toList().isEmpty()) {
            throw new RuntimeException("Room is already reserved at this time!");
        }

        if (!appointmentRepository.findByDoctorIdAndStatusNotClosed(doctorId).stream()
                .filter(a -> a.getAppointmentTime().equals(appointmentTime))
                .toList().isEmpty()) {
            throw new RuntimeException("Doctor is already booked at this time!");
        }
        // --- Save appointment ---
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setRoom(room);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setNotes(notes);
        appointment.setStatus(status);
        // Set doctor name explicitly
        appointment.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());

        return appointmentRepository.save(appointment);
    }



    public Appointment cancelAppointment(Long appointmentId, String username, String reason) {
        Appointment appointment = getAppointmentById(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.CLOSED) {
            throw new RuntimeException("Cannot cancel a CLOSED appointment");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("Appointment already cancelled");
        }

        appointment.markCancelled(reason);
        Appointment saved = appointmentRepository.save(appointment);

        logService.log(
                username,
                "CANCEL_APPOINTMENT",
                "Appointment " + appointmentId + " cancelled with reason: " + reason
        );

        return saved;
    }

    // --------------------- UPDATE APPOINTMENT ---------------------
    public Appointment updateAppointment(Long appointmentId, Appointment updated) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Long doctorId = appointment.getDoctor().getId();

        List<Appointment> doctorAppointments = appointmentRepository.findByDoctorId(doctorId);

        for (Appointment existing : doctorAppointments) {
            if (existing.getId().equals(appointmentId)) continue;

            if (existing.getAppointmentTime().equals(updated.getAppointmentTime())) {
                throw new RuntimeException("Doctor already has an appointment at this time.");
            }

            long minutes = Duration.between(existing.getAppointmentTime(), updated.getAppointmentTime()).toMinutes();
            if (Math.abs(minutes) < 10) {
                throw new RuntimeException("Doctor must have 10 minutes between appointments.");
            }
        }

        appointment.setAppointmentTime(updated.getAppointmentTime());
        appointment.setNotes(updated.getNotes());
        appointment.setDoctorName(updated.getDoctorName());

        return appointmentRepository.save(appointment);
    }

    // --------------------- GET APPOINTMENTS BY PATIENT ---------------------
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return appointmentRepository.findByPatient(patient);
    }

    // --------------------- GET APPOINTMENTS BY DATE ---------------------
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findAll()
                .stream()
                .filter(a -> a.getAppointmentTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    // --------------------- DELETE APPOINTMENT ---------------------
    public void deleteAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }

    // --------------------- GET APPOINTMENTS BY DOCTOR ---------------------
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        // Fetch doctor to ensure it exists
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Get all appointments for this doctor
        return appointmentRepository.findByDoctorId(doctorId);
    }



    // ============================================================
    // MARK ATTENDED
    // ============================================================
    public Appointment markAttended(Long appointmentId, String username) {

        Appointment appointment = getAppointmentById(appointmentId);

        appointment.setStatus(AppointmentStatus.ATTENDED);
        appointment.setAttendDate(LocalDateTime.now());
        Appointment saved = appointmentRepository.save(appointment);

        // Log the action
        logService.log(
                username,
                "MARK_ATTENDED",
                "Appointment " + appointmentId + " marked as ATTENDED"
        );

        return saved;
    }

    @Transactional
    public Visit openVisitFromAppointment(Long appointmentId) {

        Appointment appointment = getAppointmentById(appointmentId);

        // 🔒 Only ATTENDED can open a visit
        if (appointment.getStatus() != AppointmentStatus.ATTENDED) {
            throw new RuntimeException("Only ATTENDED appointment can open visit");
        }

        // 🔒 Prevent duplicate visits (important!)
        Optional<Visit> existing =
                visitRepository.findByAppointmentId(appointmentId);

        if (existing.isPresent()) {
            return existing.get();
        }

        // ✅ Create Visit
        Visit visit = new Visit();
        visit.setAppointment(appointment);
        visit.setPatient(appointment.getPatient());
        visit.setDoctor(appointment.getDoctor());
        visit.setVisitType("APPOINTMENT");
        visit.setVisitStatus(VisitStatus.CREATED);
        visit.setCheckInTime(LocalDateTime.now());
        visit.setCreatedAt(LocalDateTime.now());
        visit.setUpdatedAt(LocalDateTime.now());

        // 🔄 CRITICAL: move appointment to IN_PROGRESS
        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        appointment.setInProgressDate(LocalDateTime.now());

        appointmentRepository.save(appointment);

        return visitRepository.save(visit);
    }

    // ============================================================
    // START APPOINTMENT
    // ============================================================
//    public Appointment startAppointment(Long appointmentId, String username) {
//
//        Appointment appointment = getAppointmentById(appointmentId);
//
//        if (appointment.getStatus() != AppointmentStatus.ATTENDED) {
//            throw new RuntimeException("Patient must be marked as ATTENDED before starting the appointment.");
//        }
//
//        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
//        appointment.setInProgressDate(LocalDateTime.now());
//        Appointment saved = appointmentRepository.save(appointment);
//
//        // Log the action
//        logService.log(
//                username,
//                "START_APPOINTMENT",
//                "Appointment " + appointmentId + " moved to IN_PROGRESS"
//        );
//
//        return saved;
//    }
    @Transactional
    public Visit startAppointment(Long appointmentId, String username) {

        Appointment appointment = getAppointmentById(appointmentId);

        // 🔒 Only ATTENDED can start
        if (appointment.getStatus() != AppointmentStatus.ATTENDED) {
            throw new RuntimeException(
                    "Only ATTENDED appointment can be started"
            );
        }

        // 🔒 Prevent duplicate Visit
        Optional<Visit> existingVisit =
                visitRepository.findByAppointmentId(appointmentId);

        if (existingVisit.isPresent()) {
            return existingVisit.get(); // ✅ already started
        }

        // ================= CREATE VISIT =================
        Visit visit = new Visit();
        visit.setAppointment(appointment);
        visit.setPatient(appointment.getPatient());
        visit.setDoctor(appointment.getDoctor());
        visit.setVisitType("APPOINTMENT");
        visit.setVisitStatus(VisitStatus.CREATED);
        visit.setCheckInTime(LocalDateTime.now());
        visit.setCreatedAt(LocalDateTime.now());
        visit.setUpdatedAt(LocalDateTime.now());

        // ================= UPDATE APPOINTMENT =================
        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        appointment.setInProgressDate(LocalDateTime.now());

        appointmentRepository.save(appointment);
        Visit savedVisit = visitRepository.save(visit);

        // ================= LOG =================
        logService.log(
                username,
                "START_APPOINTMENT",
                "Appointment " + appointmentId + " started and Visit " + savedVisit.getId() + " created"
        );

        return savedVisit;
    }

    // ============================================================
    // CLOSE APPOINTMENT
    // ============================================================
    public Appointment closeAppointment(Long appointmentId, String username) {

        Appointment appointment = getAppointmentById(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.IN_PROGRESS||appointment.getStatus() != AppointmentStatus.NEW ) {
            throw new RuntimeException("Appointment must be IN_PROGRESS OR NEW before closing.");
        }

        appointment.setStatus(AppointmentStatus.CLOSED);
        appointment.setCancelDate(LocalDateTime.now());
        Appointment saved = appointmentRepository.save(appointment);

        // Log the action
        logService.log(
                username,
                "CLOSE_APPOINTMENT",
                "Appointment " + appointmentId + " closed"
        );

        return saved;
    }


}
