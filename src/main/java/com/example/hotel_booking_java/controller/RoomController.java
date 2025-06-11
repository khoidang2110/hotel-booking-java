package com.example.hotel_booking_java.controller;

import com.example.hotel_booking_java.dto.RoomDTO;
import com.example.hotel_booking_java.payload.request.BookingRequest;
import com.example.hotel_booking_java.payload.request.FindRoomAvailableRequest;
import com.example.hotel_booking_java.payload.request.RoomRequest;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.services.RoomServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomServices roomServices;
    
    @Autowired
    private JwtHelper jwt;


    @PostMapping
    public ResponseEntity<BaseResponse> createRoom(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody RoomRequest req) {

        int userId = tokenUserId(auth);
        RoomDTO dto = roomServices.createRoom(req, userId);
        return ok("Room created", dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateRoom(
            @RequestHeader("Authorization") String auth,
            @PathVariable int id,
            @RequestBody RoomRequest req) {

        int userId = tokenUserId(auth);
        RoomDTO dto = roomServices.updateRoom(id, req, userId);
        return ok("Room updated", dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteRoom(
            @RequestHeader("Authorization") String auth,
            @PathVariable int id) {

        tokenUserId(auth);           // validate token only
        roomServices.deleteRoom(id);
        return ok("Room deleted", null);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllRooms(
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {

        List<RoomDTO> list = roomServices.getAllRooms(pageNumber, pageSize);
        return ok("success", list);
    }

    @GetMapping("/available")
    public ResponseEntity<BaseResponse> findAvailableRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {

        List<RoomDTO> list = roomServices.findAvailable(checkIn, checkOut);
        return ok("success", list);
    }

    @PostMapping("/{id}/is-available")
    public ResponseEntity<BaseResponse> isRoomAvailable(
            @PathVariable int id,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {

        boolean free = roomServices.isRoomAvailable(id, checkIn, checkOut);
        return ok("success", free);
    }

    @GetMapping("/suite")
    public ResponseEntity<BaseResponse> findAvailableByCapacity(
            @Valid @ModelAttribute FindRoomAvailableRequest bookingRequest
    ) {
        List<RoomDTO> list = roomServices.findAvailableWithCapacity(bookingRequest);
        return ok("success", list);
    }

    private int tokenUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new RuntimeException("Missing token");

        String token = authHeader.substring(7);
        Map<String, Object> claims = jwt.decodeToken(token);

        if (claims == null || claims.get("id") == null)
            throw new RuntimeException("Invalid or expired token");

        return (Integer) claims.get("id");
    }

    private ResponseEntity<BaseResponse> ok(String msg, Object data) {
        BaseResponse r = new BaseResponse();
        r.setCode(200);
        r.setMessage(msg);
        r.setData(data);
        return ResponseEntity.ok(r);
    }
}
