package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.payload.request.ChangePasswordRequest;
import com.example.hotel_booking_java.payload.request.SignUpRequest;
import com.example.hotel_booking_java.payload.request.UpdateUserRequest;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.payload.response.UserInfoResponse;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.UserServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

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

    @Override
    public UserInfoResponse getUserInfoFromToken(String authHeader) throws Exception {
        String token = extractToken(authHeader);
        Map<String, Object> payload = jwtHelper.decodeToken(token);

        if (payload == null) {
            throw new RuntimeException("Token is invalid or expired");
        }

        return new UserInfoResponse(
                ((Number) payload.get("id")).longValue(),
                (String) payload.get("fullName"),
                (String) payload.get("email"),
                (String) payload.get("phone"),
                ((Number) payload.get("role_id")).intValue()
        );
    }

    @Override
    public void updateUserInfo(String authHeader, UpdateUserRequest request)  {
        String token = extractToken(authHeader);
        Map<String, Object> payload = jwtHelper.decodeToken(token);

        if (payload == null) {
            throw new RuntimeException("Token is invalid or expired");
        }

        int userId = ((Number) payload.get("id")).intValue();

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        userRepository.save(user);
    }
    @Override
    public BaseResponse changePassword(String authHeader, ChangePasswordRequest request) {
        BaseResponse response = new BaseResponse();

        String token = extractToken(authHeader);
        Map<String, Object> payload = jwtHelper.decodeToken(token);

        if (payload == null) {
            throw new RuntimeException("Token is invalid or expired");
        }

        int userId = ((Number) payload.get("id")).intValue();

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        response.setMessage("Password changed successfully");
        return response;
    }
    private String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }

}
