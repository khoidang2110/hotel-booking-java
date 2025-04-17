package com.example.hotel_booking_java.controller;

import com.example.hotel_booking_java.dto.review.ReviewCreateDto;
import com.example.hotel_booking_java.dto.review.ReviewResponseDto;
import com.example.hotel_booking_java.enums.RoomType;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.services.ReviewServices;
import com.example.hotel_booking_java.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewServices reviewServices;

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody ReviewCreateDto request) {

        BaseResponse response = new BaseResponse();

        try {
            String result = reviewServices.createReview(authHeader, request);

            if ("updated".equals(result)) {
                response.setMessage("review updated successfully");
            } else {
                response.setMessage("review created successfully");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("created failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/room-id")
    public ResponseEntity<?> getReviewByRoomId(

            @RequestParam("roomId") Long roomId) {

        BaseResponse response = new BaseResponse();

        try {
            List<ReviewResponseDto> reviews = reviewServices.getReviewsByRoomId( roomId);

            response.setMessage("Get review successfully");
            response.setData(reviews);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Get failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/room-type")
    public ResponseEntity<?> getReviewsByRoomType(@RequestParam String roomType) {
        BaseResponse response = new BaseResponse();
        try {
            RoomType typeEnum = RoomType.valueOf(roomType.toUpperCase()); // ← convert String to Enum
            List<ReviewResponseDto> result = reviewServices.getReviewsByRoomType(typeEnum);
            response.setMessage("Get reviews successfully");
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.setMessage("Invalid room type: " + roomType);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.setMessage("Get failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
}
}
