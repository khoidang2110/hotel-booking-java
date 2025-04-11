package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.payload.request.LoginRequest;
import com.example.hotel_booking_java.payload.request.SignUpRequest;

import java.util.List;

public interface UserServices {
    void signUp(SignUpRequest request);
}