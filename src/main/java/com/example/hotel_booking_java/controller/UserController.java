package com.example.hotel_booking_java.controller;


import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.UserServices;
import com.example.hotel_booking_java.services.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices userServices;

    @GetMapping
    public List<Users> getAllUser() {
        return userServices.getAllUser();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userServices.deleteUser(id);
    }

}
