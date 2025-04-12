package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.payload.request.ChangePasswordRequest;
import com.example.hotel_booking_java.payload.request.LoginRequest;
import com.example.hotel_booking_java.payload.request.SignUpRequest;
import com.example.hotel_booking_java.payload.request.UpdateUserRequest;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.payload.response.UserInfoResponse;

import java.util.List;

public interface UserServices {
    void signUp(SignUpRequest request);
    UserInfoResponse getUserInfoFromToken(String token) throws Exception;
    void updateUserInfo(String token, UpdateUserRequest request);
    BaseResponse changePassword(String authHeader, ChangePasswordRequest request);


}