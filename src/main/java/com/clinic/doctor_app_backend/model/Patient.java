    package com.clinic.doctor_app_backend.model;

    import com.clinic.doctor_app_backend.choices.PatientType;
    import jakarta.persistence.*;
    import lombok.Data;

    import java.time.LocalDate;

    @Entity
    @Data
    @Table(name = "patient")
    public class Patient {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String firstName;
        private String middleName;
        private String lastName;
        private String phone;

        private String address;
        private String gender;
        private LocalDate dateOfBirth;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private PatientType patientType;

        // Optional identifiers
        @Column(name = "passport_number", nullable = true)
        private String passportNumber;
        // New fields
        private String country;
        private String city;
        @Column(name = "insurance_provider")
        private String insuranceProvider;

        @Column(name = "class_a")
        private boolean classA;

        @Column(name = "class_b")
        private boolean classB;

        @Column(name = "class_c")
        private boolean classC;
        // helper
        public boolean isTemp() {
            return patientType == PatientType.TEMP;
        }
    }
