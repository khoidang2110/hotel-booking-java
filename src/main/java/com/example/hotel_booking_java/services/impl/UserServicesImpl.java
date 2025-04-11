package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.payload.request.SignUpRequest;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpRequest request) {

        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Users user = new Users();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoleId(1); // mặc định role là 1
        user.setCreatedBy(0);
        user.setModifiedBy(0);

        userRepository.save(user);
    }
}
