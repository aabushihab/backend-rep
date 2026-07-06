package com.clinic.doctor_app_backend.controller;


import com.clinic.doctor_app_backend.model.UserActionLog;
import com.clinic.doctor_app_backend.repository.UserActionLogRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin("*")
public class LogController {

    private final UserActionLogRepository logRepository;

    public LogController(UserActionLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @PostMapping("/add")
    public UserActionLog addLog(@RequestBody UserActionLog log) {
        // Set timestamp if not provided
        if (log.getTimestamp() == null) {
            log.setTimestamp(java.time.LocalDateTime.now());
        }
        return logRepository.save(log);
    }

    // --------------------- GET ALL LOGS ---------------------
    @GetMapping
    public List<UserActionLog> getAllLogs() {
        return logRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .toList();
    }

    // --------------------- GET LOGS BY USERNAME ---------------------
    @GetMapping("/user/{username}")
    public List<UserActionLog> getLogsByUser(@PathVariable("username") String username) {
        return logRepository.findAll()
                .stream()
                .filter(log -> log.getUsername().equalsIgnoreCase(username))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .toList();
    }

    // --------------------- GET LOGS BY DATE RANGE ---------------------
    @GetMapping("/date")
    public List<UserActionLog> getLogsByDateRange(
            @RequestParam ("from")LocalDate from,
            @RequestParam ("to") LocalDate to
    ) {
        return logRepository.findAll()
                .stream()
                .filter(log -> !log.getTimestamp().toLocalDate().isBefore(from)
                        && !log.getTimestamp().toLocalDate().isAfter(to))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .toList();
    }
}

