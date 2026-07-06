package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits/{visitId}/procedures")
@RequiredArgsConstructor
@CrossOrigin("*")

public class VisitProcedureController {

    private final VisitService service;

    @GetMapping
    public List<String> get(@PathVariable ("visitId") Long visitId) {
        return service.getProcedures(visitId);
    }

    @PostMapping
    public List<String> add(@PathVariable ("visitId") Long visitId,
                            @RequestParam ("procedure") String procedure) {
        return service.addProcedure(visitId, procedure);
    }

    @DeleteMapping
    public List<String> delete(@PathVariable ("visitId") Long visitId,
                               @RequestParam ("procedure") String procedure) {
        return service.deleteProcedure(visitId, procedure);
    }

    @PutMapping
    public List<String> update(@PathVariable ("visitId") Long visitId,
                               @RequestParam ("oldProcedure") String oldProcedure,
                               @RequestParam ("newProcedure") String newProcedure) {
        return service.updateProcedure(visitId, oldProcedure, newProcedure);
    }
}