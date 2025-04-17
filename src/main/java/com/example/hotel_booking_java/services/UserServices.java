package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.dto.user.ChangePasswordRequestDto;
import com.example.hotel_booking_java.dto.user.SignUpRequestDto;
import com.example.hotel_booking_java.dto.user.UpdateUserRequestDto;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.dto.user.UserInfoResponseDto;

public interface UserServices {
    void signUp(SignUpRequestDto request);
    UserInfoResponseDto getUserInfoFromToken(String token) throws Exception;
    void updateUserInfo(String token, UpdateUserRequestDto request);
    BaseResponse changePassword(String authHeader, ChangePasswordRequestDto request);


}