package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServices implements UserServiceImp{

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Users> getAllUser() {
        return userRepository.findAll();
    }
}
