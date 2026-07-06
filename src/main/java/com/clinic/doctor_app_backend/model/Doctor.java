package com.clinic.doctor_app_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Specialty is required")
    @Size(max = 100)
    private String specialty;

    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String email;

    @Size(max = 30)
    private String phone;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<Long> favoriteDrugIds = new HashSet<>();
    public Doctor() {}

    public Doctor(String firstName, String middleName, String lastName, String specialty,
                  String email, String phone, String username, String password) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    // GETTERS & SETTERS ...

    // ---------- GETTERS & SETTERS ----------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // ---------- OPTIONAL: FULL NAME FIELD ----------
    @Transient
    public String getFullName() {
        if (middleName == null || middleName.isBlank()) {
            return firstName + " " + lastName;
        }
        return firstName + " " + middleName + " " + lastName;
    }
}
