package com.example.hotel_booking_java.services;


import com.example.hotel_booking_java.dto.user.LoginRequestDto;

public interface AuthenticationServices {


    String login(LoginRequestDto request);

}