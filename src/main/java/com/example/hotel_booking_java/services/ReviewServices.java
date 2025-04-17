package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.dto.review.ReviewCreateDto;
import com.example.hotel_booking_java.dto.review.ReviewResponseDto;
import com.example.hotel_booking_java.dto.user.SignUpRequestDto;
import com.example.hotel_booking_java.entity.Reviews;
import com.example.hotel_booking_java.enums.RoomType;

import java.util.List;

public interface ReviewServices
{
    String createReview(String token,ReviewCreateDto request);
    List<ReviewResponseDto> getReviewsByRoomId( Long roomId);
    List<ReviewResponseDto> getReviewsByRoomType(RoomType roomType);

}
