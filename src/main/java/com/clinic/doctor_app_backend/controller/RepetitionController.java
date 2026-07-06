package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.model.Repetition;
import com.clinic.doctor_app_backend.service.RepetitionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repetitions")
@CrossOrigin(origins = "*")
public class RepetitionController {

    private final RepetitionService repetitionService;

    public RepetitionController(RepetitionService repetitionService) {
        this.repetitionService = repetitionService;
    }

    @GetMapping
    public List<Repetition> getAllRepetitions() {
        return repetitionService.getAllRepetitions();
    }
}