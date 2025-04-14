package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.dto.review.ReviewCreateDto;
import com.example.hotel_booking_java.dto.user.SignUpRequestDto;

public interface ReviewServices
{
    void createReview(String token,ReviewCreateDto request);
}
