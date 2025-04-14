package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.review.ReviewCreateDto;
import com.example.hotel_booking_java.entity.Reviews;
import com.example.hotel_booking_java.entity.Rooms;
import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.enums.RoomStatus;
import com.example.hotel_booking_java.repository.ReviewRepository;
import com.example.hotel_booking_java.repository.RoomRepository;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.ReviewServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ReviewServicesImpl implements ReviewServices {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public String createReview(String authHeader, ReviewCreateDto request) {
        String token = extractToken(authHeader);
        Map<String, Object> payload = jwtHelper.decodeToken(token);

        if (payload == null) {
            throw new RuntimeException("Token is invalid or expired");
        }

        try {
            int userId = ((Number) payload.get("id")).intValue();
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            Rooms room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found with ID: " + request.getRoomId()));

            // Check if the user has already reviewed this room
            Reviews existingReview = reviewRepository.findByUserAndRoom(user, room);
//            if (existingReview != null) {
//                // If a review exists, delete the existing review
//                reviewRepository.delete(existingReview);
//            }
            if (existingReview != null) {
                // Update existing review
                existingReview.setRating(request.getRating());
                existingReview.setComment(request.getComment());
                existingReview.setModifiedAt(LocalDateTime.now());
                existingReview.setModifiedBy(userId);
                reviewRepository.save(existingReview);
                return "updated";
            }

            Reviews review = new Reviews();
            review.setRoom(room);
            review.setUser(user);
            review.setRating(request.getRating());
            review.setComment(request.getComment());
            review.setCreatedAt(LocalDateTime.now());
            review.setModifiedAt(LocalDateTime.now());
            review.setCreatedBy(userId); // Example: Set createdBy as userId
            review.setModifiedBy(userId); // Example: Set modifiedBy as userId

            reviewRepository.save(review);
            return "created";
        } catch (Exception e) {
            throw new RuntimeException("Failed to create review: " + e.getMessage(), e);
        }
    }


    private String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }


}
