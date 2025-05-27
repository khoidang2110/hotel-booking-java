package com.example.hotel_booking_java.services;

public interface LoginAttemptServices {
    void loginFailed(String email);
    boolean isBlocked(String email);
    void loginSucceeded(String email);
}
