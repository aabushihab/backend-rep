package com.clinic.doctor_app_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
    @AllArgsConstructor
    public class TimeSlotDto {
        private LocalDateTime start;
        private LocalDateTime end;
        private boolean available;
    }
