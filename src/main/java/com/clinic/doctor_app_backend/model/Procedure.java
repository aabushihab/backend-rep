package com.clinic.doctor_app_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "procedures")
public class Procedure {

    @Id
    @Column(name = "procedure_id", nullable = false)
    private String procedureId;

    @Column(name = "procedure_name", nullable = false)
    private String procedureName;

    @Column(name = "category")
    private String category;

    // Constructors
    public Procedure() {
    }

    public Procedure(String procedureId, String procedureName, String category) {
        this.procedureId = procedureId;
        this.procedureName = procedureName;
        this.category = category;
    }

    // Getters & Setters
    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Optional: toString
    @Override
    public String toString() {
        return "Procedure{" +
                "procedureId='" + procedureId + '\'' +
                ", procedureName='" + procedureName + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}