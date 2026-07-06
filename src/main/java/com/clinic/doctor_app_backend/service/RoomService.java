package com.clinic.doctor_app_backend.service;


import com.clinic.doctor_app_backend.model.Room;
import com.clinic.doctor_app_backend.model.Section;
import com.clinic.doctor_app_backend.repository.RoomRepository;
import com.clinic.doctor_app_backend.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final SectionRepository sectionRepository;

    public RoomService(RoomRepository roomRepository, SectionRepository sectionRepository) {
        this.roomRepository = roomRepository;
        this.sectionRepository = sectionRepository;
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get rooms by section
    public List<Room> getRoomsBySection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));
        return roomRepository.findAll()
                .stream()
                .filter(r -> r.getSection().getId().equals(section.getId()))
                .toList();
    }

    // Create a room
    public Room createRoom(Room room, Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));
        room.setSection(section);
        return roomRepository.save(room);
    }

    // Update room
    public Room updateRoom(Long roomId, Room roomDetails) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomNumber(roomDetails.getRoomNumber());

        if (roomDetails.getSection() != null) {
            room.setSection(roomDetails.getSection());
        }
        if (roomDetails.getDoctor() != null) {
            room.setDoctor(roomDetails.getDoctor());
        }
        return roomRepository.save(room);
    }

    // Delete room
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
    }
}

