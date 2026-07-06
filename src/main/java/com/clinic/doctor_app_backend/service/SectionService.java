package com.clinic.doctor_app_backend.service;


import com.clinic.doctor_app_backend.model.Section;
import com.clinic.doctor_app_backend.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    // Get all sections
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    // Get one section by ID
    public Section getSectionById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section not found"));
    }

    // Create new section
    public Section createSection(Section section) {
        // Optional: check for duplicate name
        Optional<Section> existing = sectionRepository.findAll()
                .stream()
                .filter(s -> s.getName().equalsIgnoreCase(section.getName()))
                .findFirst();
        if (existing.isPresent()) {
            throw new RuntimeException("Section with this name already exists");
        }
        return sectionRepository.save(section);
    }

    // Update section
    public Section updateSection(Long id, Section sectionDetails) {
        Section section = getSectionById(id);
        section.setName(sectionDetails.getName());
        return sectionRepository.save(section);
    }

    // Delete section
    public void deleteSection(Long id) {
        Section section = getSectionById(id);
        sectionRepository.delete(section);
    }
}
