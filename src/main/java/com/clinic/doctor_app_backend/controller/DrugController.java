package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.dto.DrugFilter;
import com.clinic.doctor_app_backend.model.Drug;
import com.clinic.doctor_app_backend.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
@CrossOrigin("*")

public class DrugController {

    private final DrugService drugService;
    @PostMapping("/by-ids")
    public ResponseEntity<List<Drug>> getDrugsByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(drugService.getDrugsByIds(ids));
    }
    @PostMapping("/search")
    public Page<Drug> search(
            @RequestBody DrugFilter filter,
            Pageable pageable) {

        return drugService.search(filter, pageable);
    }


    @GetMapping("/autocomplete")
    public List<Drug> autocomplete(@RequestParam("name") String name) {
        return drugService.autocomplete(name);
    }
}