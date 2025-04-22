package com.example.hotel_booking_java.services;


import com.example.hotel_booking_java.dto.user.LoginRequestDto;
import com.example.hotel_booking_java.dto.user.LoginResponseDto;

public interface AuthenticationServices {


    LoginResponseDto login(LoginRequestDto request);

}