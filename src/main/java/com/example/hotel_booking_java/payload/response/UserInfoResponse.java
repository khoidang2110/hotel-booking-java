package com.example.hotel_booking_java.payload.response;


import lombok.Data;




@Data
public class UserInfoResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private int role_id;

    public UserInfoResponse(Long id, String fullName, String email, String phone, int role_id) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role_id = role_id;
    }

    // getters & setters
}

