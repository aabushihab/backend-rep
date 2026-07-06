package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.dto.TimeSlotDto;
import com.clinic.doctor_app_backend.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
@CrossOrigin("*")

public class AvailabilityController {

    private final AvailabilityService availabilityService;

    // doctor + room
    @GetMapping
    public List<TimeSlotDto> byDoctorAndRoom(
            @RequestParam(required = false,name = "doctorId")Long doctorId,
            @RequestParam(required = false,name = "roomId") Long roomId,
            @RequestParam (name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        if (doctorId == null && roomId == null) {
            throw new IllegalArgumentException("doctorId or roomId is required");
        }
        return availabilityService.getAvailability(doctorId, roomId, date);
    }

    // doctor only
    @GetMapping("/doctor/{doctorId}")
    public List<TimeSlotDto> byDoctor(
            @PathVariable("doctorId") Long doctorId,
            @RequestParam (name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return availabilityService.getAvailability(doctorId, null, date);
    }

    // room only
    @GetMapping("/room/{roomId}")
    public List<TimeSlotDto> byRoom(
            @PathVariable("roomId") Long roomId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return availabilityService.getAvailability(null, roomId, date);
    }

    // single time check
    @GetMapping("/check")
    public Map<String, Boolean> checkSlot(
            @RequestParam(required = false,name = "doctorId") Long doctorId,
            @RequestParam(required = false,name = "roomId") Long roomId,
            @RequestParam (name = "time")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime time
    ) {
        boolean available = availabilityService.isSlotAvailable(doctorId, roomId, time);
        return Map.of("available", available);
    }
}
