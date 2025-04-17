package com.example.hotel_booking_java.controller;

import com.example.hotel_booking_java.dto.RoomDTO;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.services.RoomServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomServices roomServices;

    @Autowired
    private JwtHelper jwtHelper;

    /**
     * Insert a new Room.
     * Expects the following parameters:
     * - roomNumber, type, price and optional description.
     * The Authorization header must be provided with a valid Bearer token.
     */
    @PostMapping
    public ResponseEntity<BaseResponse> insertRoom(
            @RequestHeader("Authorization") String authorization,
            @RequestParam String roomNumber,
            @RequestParam String type, // e.g., "SINGLE", "DOUBLE", "SUITE"
            @RequestParam BigDecimal price,
            @RequestParam(required = false) String description) {
        String token = authorization.startsWith("Bearer ") ? authorization.substring(7) : authorization;
        Map<String, Object> claims = jwtHelper.decodeToken(token);
        if (claims == null || claims.get("id") == null) {
            BaseResponse response = new BaseResponse();
            response.setCode(401);
            response.setMessage("Invalid or expired token");
            return ResponseEntity.status(401).body(response);
        }

        int userId = (int) claims.get("id");

        roomServices.insertRoom(roomNumber, type, price, description, userId);

        BaseResponse response = new BaseResponse();
        response.setCode(200);
        response.setMessage("Room inserted successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateRoom(
            @RequestHeader("Authorization") String authorization,
            @PathVariable int id,
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) String description) {

        String token = authorization.startsWith("Bearer ") ? authorization.substring(7) : authorization;
        Map<String, Object> claims = jwtHelper.decodeToken(token);
        if (claims == null || claims.get("id") == null) {
            BaseResponse response = new BaseResponse();
            response.setCode(401);
            response.setMessage("Invalid or expired token");
            return ResponseEntity.status(401).body(response);
        }

        int userId = (int) claims.get("id");

        roomServices.updateRoom(id, roomNumber, type, price, description, userId);

        BaseResponse response = new BaseResponse();
        response.setCode(200);
        response.setMessage("Room updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteRoom(
            @RequestHeader("Authorization") String authorization,
            @PathVariable int id) {

        String token = authorization.startsWith("Bearer ") ? authorization.substring(7) : authorization;
        Map<String, Object> claims = jwtHelper.decodeToken(token);
        if (claims == null || claims.get("id") == null) {
            BaseResponse response = new BaseResponse();
            response.setCode(401);
            response.setMessage("Invalid or expired token");
            return ResponseEntity.status(401).body(response);
        }

        roomServices.deleteRoom(id);

        BaseResponse response = new BaseResponse();
        response.setCode(200);
        response.setMessage("Room deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllRooms(
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {

        List<RoomDTO> roomList = roomServices.getAllRooms(pageNumber, pageSize);
        BaseResponse response = new BaseResponse();
        response.setCode(200);
        response.setMessage("Rooms retrieved successfully");
        response.setData(roomList);
        return ResponseEntity.ok(response);
    }
}
