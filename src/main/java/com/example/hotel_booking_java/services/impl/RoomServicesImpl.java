package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.RoomDTO;
import com.example.hotel_booking_java.entity.Rooms;
import com.example.hotel_booking_java.enums.RoomStatus;
import com.example.hotel_booking_java.enums.RoomType;
import com.example.hotel_booking_java.payload.request.BookingRequest;
import com.example.hotel_booking_java.payload.request.FindRoomAvailableRequest;
import com.example.hotel_booking_java.payload.request.RoomRequest;
import com.example.hotel_booking_java.repository.BookingRepo;
import com.example.hotel_booking_java.repository.RoomRepository;
import com.example.hotel_booking_java.services.RoomServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomServicesImpl implements RoomServices {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepo bookingRepo;

    private RoomDTO toDto(Rooms r) {
        RoomDTO d = new RoomDTO();
        d.setId(r.getId());
        d.setRoomNumber(r.getRoomNumber());
        d.setType(r.getType());
        d.setPrice(r.getPrice());
        d.setStatus(r.getStatus());
        d.setDescription(r.getDescription());
        d.setCreatedAt(r.getCreatedAt());
        d.setModifiedAt(r.getModifiedAt());
        d.setCreatedBy(r.getCreatedBy());
        d.setModifiedBy(r.getModifiedBy());
        return d;
    }

    @Override
    public List<RoomDTO> findAvailable(LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRooms(checkIn, checkOut)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public boolean isRoomAvailable(int roomId, LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRooms(checkIn, checkOut)
                .stream().anyMatch(r -> r.getId() == roomId);
    }

    @Override
    public RoomDTO createRoom(RoomRequest req, int userId) {
        Rooms room = new Rooms();
        room.setRoomNumber(req.getRoomNumber().trim());
        room.setType(req.getType());
        room.setPrice(req.getPrice());
        room.setDescription(req.getDescription());
        room.setStatus(RoomStatus.AVAILABLE);
        room.setCreatedBy(userId);
        room.setModifiedBy(userId);

        return toDto(roomRepository.save(room));
    }

    @Override
    public List<RoomDTO> getAllRooms(int pageNumber, int pageSize) {
        return roomRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public RoomDTO updateRoom(int id, RoomRequest req, int userId) {

        Rooms r = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (req.getRoomNumber() != null && !req.getRoomNumber().isBlank())
            r.setRoomNumber(req.getRoomNumber().trim());
        if (req.getType() != null) r.setType(req.getType());
        if (req.getPrice() != null) r.setPrice(req.getPrice());
        if (req.getDescription() != null) r.setDescription(req.getDescription());

        r.setModifiedAt(LocalDateTime.now());
        r.setModifiedBy(userId);

        return toDto(roomRepository.save(r));
    }

    @Override
    public void deleteRoom(int id) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found with id " + id);
        }
        roomRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTO> findAvailableWithCapacity(FindRoomAvailableRequest request) {
        // Extract fields from request
        var checkIn  = request.getCheckIn();
        var checkOut = request.getCheckOut();
        int adults   = request.getAdultNumber();
        int children = request.getChildNumber();

        // Reuse the existing repository method
        List<Rooms> freeRooms = roomRepository.findAvailableRooms(checkIn, checkOut);

        return freeRooms.stream()
                .filter(r -> canAccommodate(r.getType(), adults, children))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private boolean canAccommodate(RoomType type, int adults, int children) {
        return switch (type) {
            case SINGLE -> adults <= 1 && children <= 2;
            case DOUBLE -> adults <= 2 && children <= 2;
            case SUITE -> adults <= 4 && children <= 4;
        };
    }
}
