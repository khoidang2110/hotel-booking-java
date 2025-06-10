package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.dto.RoomDTO;
import com.example.hotel_booking_java.payload.request.BookingRequest;
import com.example.hotel_booking_java.payload.request.FindRoomAvailableRequest;
import com.example.hotel_booking_java.payload.request.RoomRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomServices {
    List<RoomDTO> findAvailable(LocalDate checkIn, LocalDate checkOut);
    boolean isRoomAvailable(int roomId, LocalDate checkIn, LocalDate checkOut);
    RoomDTO createRoom(RoomRequest req, int userId);
    List<RoomDTO> getAllRooms(int pageNumber, int pageSize);
    RoomDTO updateRoom(int id, RoomRequest req, int userId);
    void deleteRoom(int id);
    List<RoomDTO> findAvailableWithCapacity(FindRoomAvailableRequest request);
}
