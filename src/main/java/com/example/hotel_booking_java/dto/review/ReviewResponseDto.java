package com.example.hotel_booking_java.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDto {
    private Long reviewId;
    private Long roomId;
    private String roomNumber; // ← mới thêm
    private int rating;
    private String comment;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
}