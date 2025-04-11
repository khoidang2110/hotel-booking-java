package com.example.hotel_booking_java.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;




@Data
@AllArgsConstructor
public class UserInfoResponse {
    private int id;
    private String fullName;
    private String email;
    private String phone;
}
