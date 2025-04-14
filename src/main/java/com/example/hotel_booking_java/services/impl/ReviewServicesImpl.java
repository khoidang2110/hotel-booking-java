package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.review.ReviewCreateDto;
import com.example.hotel_booking_java.dto.review.ReviewResponseDto;
import com.example.hotel_booking_java.entity.Reviews;
import com.example.hotel_booking_java.entity.Rooms;
import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.enums.RoomStatus;
import com.example.hotel_booking_java.enums.RoomType;
import com.example.hotel_booking_java.repository.ReviewRepository;
import com.example.hotel_booking_java.repository.RoomRepository;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.ReviewServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                existingReview.setCreatedAt(LocalDateTime.now());

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

    @Override
    public List<ReviewResponseDto> getReviewsByRoomId( Long roomId) {
        List<Reviews> reviews = reviewRepository.findByRoomId(roomId);

        return reviews.stream().map(review -> {
            ReviewResponseDto dto = new ReviewResponseDto();
            dto.setReviewId((long) review.getId());
            dto.setRoomId((long) review.getRoom().getId());
            dto.setUserId((long) review.getUser().getId());
            dto.setRoomNumber(review.getRoom().getRoomNumber());
            dto.setCreatedAt(review.getCreatedAt());

            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setUserName(review.getUser().getFullName());

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDto> getReviewsByRoomType(RoomType roomType) {
        List<Reviews> reviews = reviewRepository.findByRoom_Type(roomType);

        return reviews.stream().map(review -> {
            ReviewResponseDto dto = new ReviewResponseDto();
            dto.setReviewId((long) review.getId());
            dto.setRoomId((long) review.getRoom().getId());
            dto.setRoomNumber(review.getRoom().getRoomNumber());
            dto.setRating(review.getRating());
            dto.setCreatedAt(review.getCreatedAt());
            dto.setComment(review.getComment());
            dto.setUserName(review.getUser().getFullName());
            return dto;
        }).collect(Collectors.toList());
    }
    private String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }


}
