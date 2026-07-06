    package com.clinic.doctor_app_backend.controller;
    import com.clinic.doctor_app_backend.dto.ChangePasswordRequest;
    import com.clinic.doctor_app_backend.model.Doctor;
    import com.clinic.doctor_app_backend.service.DoctorService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Set;

    @RestController
    @RequestMapping("/api/doctors")
    @CrossOrigin("*")
    public class DoctorController {

        private final DoctorService doctorService;

        public DoctorController(DoctorService doctorService) {
            this.doctorService = doctorService;
        }


        @PostMapping("/{doctorId}/favorites")
        public ResponseEntity<?> addFavorites(        @PathVariable("doctorId") Long doctorId,
                                                      @RequestBody Set<Long> drugIds) {

            return ResponseEntity.ok(
                    doctorService.addFavoriteDrugIds(doctorId, drugIds)
            );
        }
        @DeleteMapping("/{doctorId}/favorites")
        public ResponseEntity<?> removeFavorites(        @PathVariable("doctorId") Long doctorId,

                                                         @RequestBody Set<Long> drugIds) {

            return ResponseEntity.ok(
                    doctorService.removeFavoriteDrugIds(doctorId, drugIds)
            );
        }
        @GetMapping("/{doctorId}/favorites")
        public ResponseEntity<Set<Long>> getFavorites(        @PathVariable("doctorId") Long doctorId) {
            return ResponseEntity.ok(
                    doctorService.getFavoriteDrugIds(doctorId)
            );
        }
        @PostMapping
        public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
            return ResponseEntity.ok(doctorService.addDoctor(doctor));
        }
        @PutMapping("/change-password")
        public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
            boolean changed = doctorService.changePassword(
                    request.getUsername(),
                    request.getOldPassword(),
                    request.getNewPassword()
            );

            if (changed) return ResponseEntity.ok("Password changed successfully!");
            else return ResponseEntity.badRequest().body("Old password is incorrect or doctor not found");
        }

        @GetMapping
        public ResponseEntity<List<Doctor>> getAllDoctors() {
            return ResponseEntity.ok(doctorService.getAllDoctors());
        }

        @PutMapping("/{id}")
        public ResponseEntity<Doctor> updateDoctor(@PathVariable ("id") Long id, @RequestBody Doctor doctor) {
            Doctor updated = doctorService.updateDoctor(id, doctor);
            return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteDoctor(@PathVariable ("id") Long id) {
            String result = doctorService.deleteDoctor(id);

            if (result.equals("Doctor deleted successfully!")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody Doctor doctor) {
            Doctor d = doctorService.login(doctor.getUsername(), doctor.getPassword());

            if (d != null) {
                return ResponseEntity.ok(d);
            } else {
                return ResponseEntity
                        .status(401)
                        .body("Invalid username or password");
            }
        }

    }
