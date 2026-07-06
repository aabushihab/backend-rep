package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.model.Room;
import com.clinic.doctor_app_backend.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin("*")

public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Get all rooms
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // Get rooms by section
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<Room>> getRoomsBySection(@PathVariable("sectionId") Long sectionId) {
        try {
            List<Room> rooms = roomService.getRoomsBySection(sectionId);
            return ResponseEntity.ok(rooms);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Create new room
    @PostMapping("/section/{sectionId}")
    public ResponseEntity<Room> createRoom(@RequestBody Room room, @PathVariable ("sectionId") Long sectionId) {
        try {
            Room created = roomService.createRoom(room, sectionId);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Update room
    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable ("roomId") Long roomId, @RequestBody Room room) {
        try {
            Room updated = roomService.updateRoom(roomId, room);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Delete room
    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable ("roomId") Long roomId) {
        try {
            roomService.deleteRoom(roomId);
            return ResponseEntity.ok("Room deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


}
