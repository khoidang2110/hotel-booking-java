package com.example.hotel_booking_java.services;


import com.example.hotel_booking_java.payload.request.LoginRequest;

public interface AuthenticationServices {


    String login(LoginRequest request);

}