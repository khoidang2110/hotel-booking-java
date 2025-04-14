package com.example.hotel_booking_java.controller;

import com.example.hotel_booking_java.dto.review.ReviewCreateDto;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.services.ReviewServices;
import com.example.hotel_booking_java.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewServices reviewServices;

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestHeader("Authorization") String authHeader,
                                          @Valid @RequestBody ReviewCreateDto request) {

        BaseResponse response = new BaseResponse();

        try {
            reviewServices.createReview(authHeader, request);
            response.setMessage("review created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("created failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
