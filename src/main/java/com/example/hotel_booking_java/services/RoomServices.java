package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.dto.RoomDTO;

import java.math.BigDecimal;
import java.util.List;

public interface RoomServices {
    void insertRoom(String roomNumber, String type, BigDecimal price, String description, int userId);
    List<RoomDTO> getAllRooms(int pageNumber, int pageSize);
    void updateRoom(int id, String roomNumber, String type, BigDecimal price, String description, int userId);
    void deleteRoom(int id);
}
