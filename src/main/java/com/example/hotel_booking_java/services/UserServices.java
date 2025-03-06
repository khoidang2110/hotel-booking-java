package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.entity.Users;
import java.util.List;

public interface UserServices {
    List<Users> getAllUser();
    void deleteUser(int id);
}