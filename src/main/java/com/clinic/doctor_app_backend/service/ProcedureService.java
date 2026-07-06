package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.dto.ProcedureNameDto;
import com.clinic.doctor_app_backend.model.Procedure;
import com.clinic.doctor_app_backend.repository.ProcedureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcedureService {

    private final ProcedureRepository procedureRepository;

    public ProcedureService(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    public List<Procedure> getAllProcedures() {
        return procedureRepository.findAll();
    }

    public List<Procedure> getProceduresByCategory(String category) {
        return procedureRepository.findByCategoryIgnoreCase(category);
    }

    // =========================
    // 🔍 CATEGORY SEARCH METHODS
    // =========================

    public List<ProcedureNameDto> searchRadiology(String name) {
        return procedureRepository.searchByCategoryAndName("RADIOLOGY", name);
    }

    public List<ProcedureNameDto> searchLaboratory(String name) {
        return procedureRepository.searchByCategoryAndName("LABORATORY", name);
    }

    public List<ProcedureNameDto> searchMedical(String name) {
        return procedureRepository.searchByCategoryAndName("MEDICAL", name);
    }
    // CREATE
    public Procedure addProcedure(Procedure p) {
        return procedureRepository.save(p);
    }

    // UPDATE
    public Procedure updateProcedure(String id, Procedure req) {
        Procedure p = procedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedure not found"));

        p.setProcedureName(req.getProcedureName());
        p.setCategory(req.getCategory());

        return procedureRepository.save(p);
    }

    // DELETE
    public void deleteProcedure(String id) {
        procedureRepository.deleteById(id);
    }

    // GET ALL
    public List<Procedure> getAll() {
        return procedureRepository.findAll();
    }

    // GET BY ID
    public Procedure getById(String id) {
        return procedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedure not found"));
    }


}