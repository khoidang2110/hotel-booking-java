package com.example.hotel_booking_java.dto.user;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private String fullName;

    public LoginResponseDto(String token, String fullName) {
        this.token = token;
        this.fullName = fullName;
    }

    // Getters and setters (hoặc dùng Lombok: @Data)
}

