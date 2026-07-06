package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.model.Section;
import com.clinic.doctor_app_backend.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@CrossOrigin("*")

public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // Get all sections
    @GetMapping
    public ResponseEntity<List<Section>> getAllSections() {
        return ResponseEntity.ok(sectionService.getAllSections());
    }

    // Get section by id
    @GetMapping("/{id}")
    public ResponseEntity<Section> getSectionById(@PathVariable ("id") Long id) {
        try {
            Section section = sectionService.getSectionById(id);
            return ResponseEntity.ok(section);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Create new section
    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody Section section) {
        try {
            Section created = sectionService.createSection(section);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Update section
    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable ("id") Long id, @RequestBody Section section) {
        try {
            Section updated = sectionService.updateSection(id, section);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete section
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSection(@PathVariable ("id") Long id) {
        try {
            sectionService.deleteSection(id);
            return ResponseEntity.ok("Section deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
