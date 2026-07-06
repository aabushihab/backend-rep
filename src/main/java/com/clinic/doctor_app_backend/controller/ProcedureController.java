package com.clinic.doctor_app_backend.controller;


    import com.clinic.doctor_app_backend.dto.ProcedureNameDto;
    import com.clinic.doctor_app_backend.model.Procedure;
    import com.clinic.doctor_app_backend.service.ProcedureService;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/procedures")
    @CrossOrigin("*")
    public class ProcedureController {

        private final ProcedureService procedureService;

        public ProcedureController(ProcedureService procedureService) {
            this.procedureService = procedureService;
        }


        // =========================
        // CREATE PROCEDURE
        // =========================
        @PostMapping
        public Procedure createProcedure(@RequestBody Procedure p) {
            return procedureService.addProcedure(p);
        }

        // =========================
        // UPDATE PROCEDURE
        // =========================
        @PutMapping("/{id}")
        public Procedure updateProcedure(
                @PathVariable String id,
                @RequestBody Procedure req
        ) {
            return procedureService.updateProcedure(id, req);
        }

        // =========================
        // DELETE PROCEDURE
        // =========================
        @DeleteMapping("/{id}")
        public void deleteProcedure(@PathVariable String id) {
            procedureService.deleteProcedure(id);
        }



    // =========================
    // GET PROCEDURE BY ID
    // =========================
    @GetMapping("/{id}")
    public Procedure getProcedureById(@PathVariable String id) {
        return procedureService.getById(id);
    }

    @GetMapping
    public List<Procedure> getProcedures(
            @RequestParam(required = false,name = "category") String category) {

        if (category == null || category.isBlank()) {
            return procedureService.getAllProcedures();
        }

        return procedureService.getProceduresByCategory(category);
    }

    @GetMapping("/search")
    public List<ProcedureNameDto> search(
            @RequestParam ("category") String category,
            @RequestParam ("name") String name
    ) {

        switch (category.toUpperCase()) {

            case "RADIOLOGY":
                return procedureService.searchRadiology(name);

            case "LABORATORY":
                return procedureService.searchLaboratory(name);

            case "MEDICAL":
                return procedureService.searchMedical(name);

            default:
                return List.of();
        }
    }
}