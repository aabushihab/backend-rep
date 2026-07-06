package com.clinic.doctor_app_backend.service;


import com.clinic.doctor_app_backend.choices.VisitStatus;
import com.clinic.doctor_app_backend.choices.VisitType;
import com.clinic.doctor_app_backend.model.*;
import com.clinic.doctor_app_backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalkINService {

    private final WalkInRepository visitRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final RoomRepository roomRepository;
    private final LogService logService;
    private final VisitRepository visitRepositoryForVisit;



    public WalkINService(WalkInRepository visitRepository,
                         PatientRepository patientRepository,
                         DoctorRepository doctorRepository,
                         RoomRepository roomRepository, LogService logService, VisitRepository visitRepositoryForVisit) {
        this.visitRepository = visitRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.roomRepository = roomRepository;
        this.logService = logService;
        this.visitRepositoryForVisit = visitRepositoryForVisit;
    }
    // ------------------- CLOSE VISIT -------------------
    public WalkInModel closeVisit(Long visitId) {

        WalkInModel visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        if (visit.getStatus() == VisitStatus.CLOSED) {
            throw new RuntimeException("Visit already closed");
        }

        visit.setStatus(VisitStatus.CLOSED);
        return visitRepository.save(visit);
    }

    // ------------------- CREATE VISIT -------------------
    public WalkInModel createVisit(Long patientId, Long doctorId, Long roomId,
                                   LocalDateTime visitTime, String notes, VisitType type) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        WalkInModel visit = new WalkInModel();
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setRoom(room);
        visit.setVisitTime(visitTime);
        visit.setNotes(notes);
        visit.setType(type);
        visit.setStatus(VisitStatus.CREATED); // ✅
        visit.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
//        visit.setCreatedAt(LocalDateTime.now());
        return visitRepository.save(visit);
    }


    // ------------------- GET VISITS BY PATIENT -------------------
    public List<WalkInModel> getVisitsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return visitRepository.findByPatient(patient);
    }

    // ------------------- UPDATE VISIT -------------------
    public WalkInModel updateVisit(Long visitId, Long doctorId, Long roomId,
                                   LocalDateTime visitTime, String notes, VisitType type) {

        WalkInModel visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // ✅ Doctor conflict (ACTIVE only)
        List<WalkInModel> doctorVisits =
                visitRepository.findActiveByDoctorAndTime(doctorId, visitTime);

        doctorVisits.removeIf(v -> v.getId().equals(visitId));
        if (!doctorVisits.isEmpty())
            throw new RuntimeException("Doctor is busy at this time");

        // ✅ Room conflict (ACTIVE only)
        List<WalkInModel> roomVisits =
                visitRepository.findActiveByRoomAndTime(roomId, visitTime);

        roomVisits.removeIf(v -> v.getId().equals(visitId));
        if (!roomVisits.isEmpty())
            throw new RuntimeException("Room is occupied at this time");

        visit.setDoctor(doctor);
        visit.setRoom(room);
        visit.setVisitTime(visitTime);
        visit.setNotes(notes);
        visit.setType(type);
        visit.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());

        return visitRepository.save(visit);
    }


    // ------------------- DELETE VISIT -------------------
    public void deleteVisit(Long visitId) {
        visitRepository.deleteById(visitId);
    }

    // ------------------- GET VISITS BY DOCTOR -------------------
    public List<WalkInModel> getVisitsByDoctor(Long doctorId) {
        return visitRepository.findByDoctorId(doctorId);
    }
    public WalkInModel cancelVisit(Long visitId, String username, String reason) {
        WalkInModel visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        if (visit.getStatus() == VisitStatus.CLOSED) {
            throw new RuntimeException("Cannot cancel a CLOSED visit");
        }
        if (visit.getStatus() == VisitStatus.CANCELLED) {
            throw new RuntimeException("Visit already cancelled");
        }

        visit.markCancelled(reason);
        WalkInModel saved = visitRepository.save(visit);

        // Optional: log the cancellation
        logService.log(
                username,
                "CANCEL_VISIT",
                "Visit " + visitId + " cancelled with reason: " + reason
        );

        return saved;
    }

    // ------------------- START VISIT -------------------
    @Transactional
    public WalkInModel startVisit(Long visitId, String username, Long walkinId) {

        WalkInModel visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found with ID: " + visitId));

        if (visit.getStatus() == VisitStatus.IN_PROGRESS) {
            throw new RuntimeException("Visit is already in progress");
        }
        if (visit.getStatus() == VisitStatus.CLOSED) {
            throw new RuntimeException("Cannot start a closed visit");
        }
        if (visit.getStatus() == VisitStatus.CANCELLED) {
            throw new RuntimeException("Cannot start a cancelled visit");
        }

        // ✅ Walk-In handling
        if (walkinId != null) {
            WalkInModel walkIn = visitRepository.findById(walkinId)
                    .orElseThrow(() -> new RuntimeException("WalkIn not found: " + walkinId));

            visit.setDoctor(walkIn.getDoctor());
            visit.setRoom(walkIn.getRoom());
        }

        if (visit.getDoctor() == null || visit.getRoom() == null) {
            throw new RuntimeException("Doctor or Room missing for visit " + visitId);
        }

        visit.markInProgress(visit.getDoctor(), visit.getRoom());

        WalkInModel saved = visitRepository.save(visit);

        logService.log(
                username,
                "START_VISIT",
                "Visit " + visitId + " started"
        );

        return saved;
    }

    @Transactional
    public Visit createVisitFromWalkIn(Long walkInId) {

        WalkInModel walkIn = visitRepository.findById(walkInId)
                .orElseThrow(() -> new RuntimeException("WalkIn not found"));

        // ✅ Return existing visit if it exists
        if (walkIn.getVisit() != null) {
            return walkIn.getVisit();
        }

        // 1️⃣ Create Visit using helper
        Visit visit = walkIn.createLinkedVisit();

        walkIn.setStatus(VisitStatus.IN_PROGRESS);
        walkIn.setInProgressAt(LocalDateTime.now());
        // 2️⃣ Persist Visit
        return visitRepositoryForVisit.save(visit);

    }





}
