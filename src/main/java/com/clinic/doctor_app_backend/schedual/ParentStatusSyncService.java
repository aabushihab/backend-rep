//package com.clinic.doctor_app_backend.schedual;
//
//
//import com.clinic.doctor_app_backend.model.Appointment;
//import com.clinic.doctor_app_backend.model.Visit;
//import com.clinic.doctor_app_backend.model.WalkInModel;
//import com.clinic.doctor_app_backend.choices.AppointmentStatus;
//import com.clinic.doctor_app_backend.choices.VisitStatus;
//import com.clinic.doctor_app_backend.repository.AppointmentRepository;
//import com.clinic.doctor_app_backend.repository.WalkInRepository;
//import com.clinic.doctor_app_backend.repository.VisitRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ParentStatusSyncService {
//
//    private final VisitRepository visitRepository;
//    private final AppointmentRepository appointmentRepository;
//    private final WalkInRepository walkInRepository;
//
//    @Scheduled(fixedRate = 30_000) // every 30 seconds
//    public void syncParentStatusWithVisit() {
//
//        // Get all visits that are CLOSED or CANCELLED
//        List<Visit> visits = visitRepository.findAll();
//        for (Visit visit : visits) {
//            if (visit.getVisitStatus() == VisitStatus.CLOSED || visit.getVisitStatus() == VisitStatus.CANCELLED) {
//
//                // ---------------- Appointment ----------------
//                Appointment appointment = visit.getAppointment();
//                if (appointment != null && appointment.getStatus() != AppointmentStatus.CLOSED
//                        && appointment.getStatus() != AppointmentStatus.CANCELLED) {
//                    appointment.setStatus(AppointmentStatus.CLOSED); // or CANCELLED if you want
//                    appointment.setClosedDate(LocalDateTime.now());
//                    appointmentRepository.save(appointment);
//                    System.out.println("Appointment closed for visit: " + visit.getId());
//                }
//
//                // ---------------- WalkIn ----------------
//                WalkInModel walkIn = visit.getWalkIn();
//                if (walkIn != null && walkIn.getStatus() != VisitStatus.CLOSED
//                        && walkIn.getStatus() != VisitStatus.CANCELLED) {
//                    walkIn.setStatus(VisitStatus.CLOSED);
//                    walkIn.setClosedAt(LocalDateTime.now());
//                    walkInRepository.save(walkIn);
//                    System.out.println("Walk-in closed for visit: " + visit.getId());
//                }
//            }
//        }
//    }
//}
