package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.dto.TimeSlotDto;
import com.clinic.doctor_app_backend.repository.AppointmentRepository;
import com.clinic.doctor_app_backend.repository.WalkInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AppointmentRepository appointmentRepo;
    private final WalkInRepository walkInRepo;

    @Value("${schedule.buffer.minutes}")
    private int visitMinutes;

    @Value("${clinic.open.hour}")
    private LocalTime openHour;

    @Value("${clinic.close.hour}")
    private LocalTime closeHour;

    /**
     * Full-day availability
     * Supports:
     * - doctor only
     * - room only
     * - doctor + room
     */
    public List<TimeSlotDto> getAvailability(
            Long doctorId,
            Long roomId,
            LocalDate date
    ) {
        List<TimeSlotDto> slots = new ArrayList<>();

        LocalDateTime dayStart = LocalDateTime.of(date, openHour);
        LocalDateTime dayEnd   = LocalDateTime.of(date, closeHour);

        LocalDateTime slotStart = dayStart;

        while (!slotStart.plusMinutes(visitMinutes).isAfter(dayEnd)) {

            LocalDateTime slotEnd = slotStart.plusMinutes(visitMinutes);

            boolean available = isSlotAvailable(doctorId, roomId, slotStart);

            slots.add(new TimeSlotDto(slotStart, slotEnd, available));

            slotStart = slotEnd;
        }

        return slots;
    }

    /**
     * Single slot availability check
     */
    public boolean isSlotAvailable(
            Long doctorId,
            Long roomId,
            LocalDateTime slotStart
    ) {
        LocalDateTime slotEnd = slotStart.plusMinutes(visitMinutes);

        boolean hasAppointmentConflict =
                !appointmentRepo.findConflicts(
                        doctorId, roomId, slotStart, slotEnd
                ).isEmpty();

        if (hasAppointmentConflict) {
            return false;
        }

        boolean hasWalkInConflict =
                !walkInRepo.findConflicts(
                        doctorId, roomId, slotStart, slotEnd
                ).isEmpty();

        return !hasWalkInConflict;
    }
}
