package com.clinic.doctor_app_backend.dto;

import java.util.List;


import java.time.LocalDateTime;

public class PatientScheduleDTO {

    private Long patientId;
    private List<AppointmentDTO> appointments;
    private List<VisitDTO> visits;

    // Constructors
    public PatientScheduleDTO(Long patientId, List<AppointmentDTO> appointments, List<VisitDTO> visits) {
        this.patientId = patientId;
        this.appointments = appointments;
        this.visits = visits;
    }

    // Getters and setters
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public List<AppointmentDTO> getAppointments() { return appointments; }
    public void setAppointments(List<AppointmentDTO> appointments) { this.appointments = appointments; }
    public List<VisitDTO> getVisits() { return visits; }
    public void setVisits(List<VisitDTO> visits) { this.visits = visits; }

    // Inner DTO classes
    public static class AppointmentDTO {
        private Long id;
        private LocalDateTime appointmentTime;
        private String doctorName;
        private String roomNumber;

        public AppointmentDTO(Long id, LocalDateTime appointmentTime, String doctorName, String roomNumber) {
            this.id = id;
            this.appointmentTime = appointmentTime;
            this.doctorName = doctorName;
            this.roomNumber = roomNumber;
        }

        public Long getId() { return id; }
        public LocalDateTime getAppointmentTime() { return appointmentTime; }
        public String getDoctorName() { return doctorName; }
        public String getRoomNumber() { return roomNumber; }
    }

    public static class VisitDTO {
        private Long id;
        private LocalDateTime visitTime;
        private String doctorName;
        private String roomNumber;

        public VisitDTO(Long id, LocalDateTime visitTime, String doctorName, String roomNumber) {
            this.id = id;
            this.visitTime = visitTime;
            this.doctorName = doctorName;
            this.roomNumber = roomNumber;
        }

        public Long getId() { return id; }
        public LocalDateTime getVisitTime() { return visitTime; }
        public String getDoctorName() { return doctorName; }
        public String getRoomNumber() { return roomNumber; }
    }
}

