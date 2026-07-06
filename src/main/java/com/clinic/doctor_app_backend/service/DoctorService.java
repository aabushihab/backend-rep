package com.clinic.doctor_app_backend.service;



import com.clinic.doctor_app_backend.model.Doctor;
import com.clinic.doctor_app_backend.repository.DoctorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    public Doctor addFavoriteDrugIds(Long doctorId, Set<Long> drugIds) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (doctor.getFavoriteDrugIds() == null) {
            doctor.setFavoriteDrugIds(new HashSet<>());
        }

        doctor.getFavoriteDrugIds().addAll(drugIds);

        return doctorRepository.save(doctor);
    }
    public Doctor removeFavoriteDrugIds(Long doctorId, Set<Long> drugIds) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (doctor.getFavoriteDrugIds() != null) {
            doctor.getFavoriteDrugIds().removeAll(drugIds);
        }

        return doctorRepository.save(doctor);
    }
    public Set<Long> getFavoriteDrugIds(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return doctor.getFavoriteDrugIds() != null
                ? doctor.getFavoriteDrugIds()
                : new HashSet<>();
    }
    // CREATE doctor
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // GET all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

//    // UPDATE doctor
//    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
//        return doctorRepository.findById(id)
//                .map(existingDoctor -> {
//                    existingDoctor.setFirstName(updatedDoctor.getFirstName());
//                    existingDoctor.setMiddleName(updatedDoctor.getMiddleName());
//                    existingDoctor.setLastName(updatedDoctor.getLastName());
//                    existingDoctor.setSpecialty(updatedDoctor.getSpecialty());
//                    return doctorRepository.save(existingDoctor);
//                })
//                .orElse(null);
//    }

    // UPDATE doctor
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        return doctorRepository.findById(id)
                .map(existingDoctor -> {
                    // Only update allowed fields
                    if (updatedDoctor.getFirstName() != null)
                        existingDoctor.setFirstName(updatedDoctor.getFirstName());
                    if (updatedDoctor.getMiddleName() != null)
                        existingDoctor.setMiddleName(updatedDoctor.getMiddleName());
                    if (updatedDoctor.getLastName() != null)
                        existingDoctor.setLastName(updatedDoctor.getLastName());
                    if (updatedDoctor.getSpecialty() != null)
                        existingDoctor.setSpecialty(updatedDoctor.getSpecialty());
                    if (updatedDoctor.getEmail() != null)
                        existingDoctor.setEmail(updatedDoctor.getEmail());
                    if (updatedDoctor.getPhone() != null)
                        existingDoctor.setPhone(updatedDoctor.getPhone());

                    // Optional: update password if provided
                    if (updatedDoctor.getPassword() != null && !updatedDoctor.getPassword().isEmpty()) {
                        existingDoctor.setPassword(updatedDoctor.getPassword());
                    }

                    // Optional: prevent changing username here
                    // existingDoctor.setUsername(updatedDoctor.getUsername());

                    return doctorRepository.save(existingDoctor);
                })
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID " + id));
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }

    // DELETE doctor
    public String deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            return "Doctor not found!";
        }

        try {
            doctorRepository.deleteById(id);
            return "Doctor deleted successfully!";
        } catch (DataIntegrityViolationException ex) {
            // This happens if the doctor has appointments
            return "Cannot delete doctor. Doctor has existing appointments!";
        } catch (Exception ex) {
            return "Error deleting doctor: " + ex.getMessage();
        }
    }
    // ----------------- Change Password -----------------
//    public boolean changePassword(String username, String oldPassword, String newPassword) {
//        Doctor doctor = doctorRepository.findByUsername(username);
//
//        if (doctor == null) return false;
//
//        // Check if old password matches (plain text)
//        if (!doctor.getPassword().equals(oldPassword)) {
//            return false;
//        }
//
//        // Set new password directly
//        doctor.setPassword(newPassword);
//        doctorRepository.save(doctor);
//
//        return true;
//    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<Doctor> optionalDoctor = doctorRepository.findByUsernameIgnoreCase(username.trim());
        if (optionalDoctor.isEmpty()) {
            return false; // doctor not found
        }

        Doctor doctor = optionalDoctor.get();

        // Check old password
        if (!doctor.getPassword().equals(oldPassword)) {
            return false; // old password incorrect
        }

        doctor.setPassword(newPassword);
        doctorRepository.save(doctor);

        return true;
    }



    public Doctor login(String username, String password) {
        Doctor d = doctorRepository.findByUsername(username.trim());
        if(d != null && d.getPassword() != null && d.getPassword().trim().equals(password.trim())) {
            return d;
        }
        return null;
    }
}
