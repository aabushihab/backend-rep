package com.clinic.doctor_app_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "repetition")
public class Repetition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "repetition_desc", nullable = false)
    private String repetitionDesc;

    @Column(name = "repetition_code", nullable = false, unique = true)
    private String repetitionCode;

    public Repetition() {
    }

    public Repetition(Long id, String repetitionDesc, String repetitionCode) {
        this.id = id;
        this.repetitionDesc = repetitionDesc;
        this.repetitionCode = repetitionCode;
    }

    public Long getId() {
        return id;
    }

    public String getRepetitionDesc() {
        return repetitionDesc;
    }

    public void setRepetitionDesc(String repetitionDesc) {
        this.repetitionDesc = repetitionDesc;
    }

    public String getRepetitionCode() {
        return repetitionCode;
    }

    public void setRepetitionCode(String repetitionCode) {
        this.repetitionCode = repetitionCode;
    }
}