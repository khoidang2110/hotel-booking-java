package com.example.hotel_booking_java.services.impl;



import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.payload.request.LoginRequest;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.AuthenticationServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServicesImpl implements AuthenticationServices {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public String login(LoginRequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        // Generate token using user's email or id
        return jwtHelper.generateToken(user.getEmail());
    }
}

