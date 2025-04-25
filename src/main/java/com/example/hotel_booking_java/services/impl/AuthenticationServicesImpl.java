package com.example.hotel_booking_java.services.impl;



import com.example.hotel_booking_java.dto.user.LoginResponseDto;
import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.dto.user.LoginRequestDto;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.AuthenticationServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServicesImpl implements AuthenticationServices {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        // Generate token using user
        String token = jwtHelper.generateToken(user);
        String fullName = user.getFullName();

        return new LoginResponseDto(token, fullName);
    }
}

