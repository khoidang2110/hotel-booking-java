package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.RoomDTO;
import com.example.hotel_booking_java.entity.Rooms;
import com.example.hotel_booking_java.enums.RoomStatus;
import com.example.hotel_booking_java.enums.RoomType;
import com.example.hotel_booking_java.repository.RoomRepository;
import com.example.hotel_booking_java.services.RoomServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServicesImpl implements RoomServices {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void insertRoom(String roomNumber, String type, BigDecimal price, String description, int userId) {
        try {
            Rooms room = new Rooms();
            room.setRoomNumber(roomNumber);
            room.setType(RoomType.valueOf(type.toUpperCase()));
            room.setPrice(price);
            room.setDescription(description);
            room.setStatus(RoomStatus.AVAILABLE);
            room.setCreatedAt(LocalDateTime.now());
            room.setModifiedAt(LocalDateTime.now());
            room.setCreatedBy(userId);
            room.setModifiedBy(userId);

            roomRepository.save(room);
        } catch (Exception e) {
            throw new RuntimeException("Room insertion failed: " + e.getMessage());
        }
    }

    @Override
    public List<RoomDTO> getAllRooms(int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        Page<Rooms> roomPage = roomRepository.findAll(pageable);
        return roomPage.getContent().stream()
                .map(room -> {
                    RoomDTO dto = new RoomDTO();
                    dto.setId(room.getId());
                    dto.setRoomNumber(room.getRoomNumber());
                    dto.setType(room.getType());
                    dto.setPrice(room.getPrice());
                    dto.setDescription(room.getDescription());
                    dto.setStatus(room.getStatus());
                    // Optionally, set createdAt, modifiedAt, createdBy, modifiedBy if needed
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateRoom(int id, String roomNumber, String type, BigDecimal price, String description, int userId) {
        Optional<Rooms> roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty()) {
            throw new RuntimeException("Room not found with id " + id);
        }
        Rooms room = roomOpt.get();

        if (roomOpt.get().getRoomNumber() != null && !roomOpt.get().getRoomNumber().trim().isEmpty()) {
            room.setRoomNumber(roomOpt.get().getRoomNumber());
        }

        if (roomOpt.get().getType() != null) {
            room.setType(roomOpt.get().getType());
        }

        if (roomOpt.get().getPrice() != null) {
            room.setPrice(roomOpt.get().getPrice());
        }

        if (roomOpt.get().getDescription() != null && !roomOpt.get().getDescription().trim().isEmpty()) {
            room.setDescription(roomOpt.get().getDescription());
        }

        room.setModifiedAt(LocalDateTime.now());
        room.setModifiedBy(userId);

        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(int id) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found with id " + id);
        }
        roomRepository.deleteById(id);
    }
}
