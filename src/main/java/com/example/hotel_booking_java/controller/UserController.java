package com.example.hotel_booking_java.controller;


import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @GetMapping
    public List<Users> getAllUser() {
        return userRepository.findAll();
    }

}
