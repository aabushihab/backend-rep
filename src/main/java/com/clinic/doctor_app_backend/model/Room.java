package com.clinic.doctor_app_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"room_number", "section_id"})
        }
)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;  // "101", "A12", etc.

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = true)
    private Doctor doctor;  // Optional: doctor assigned to this room
}

