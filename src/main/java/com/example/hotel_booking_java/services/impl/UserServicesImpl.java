package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.dto.user.ChangePasswordRequestDto;
import com.example.hotel_booking_java.dto.user.SignUpRequestDto;
import com.example.hotel_booking_java.dto.user.UpdateUserRequestDto;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.dto.user.UserInfoResponseDto;
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
    public void signUp(SignUpRequestDto request) {

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
    public UserInfoResponseDto getUserInfoFromToken(String authHeader) throws Exception {
        String token = jwtHelper.extractToken(authHeader);
        Map<String, Object> payload = jwtHelper.decodeToken(token);

        if (payload == null) {
            throw new RuntimeException("Token is invalid or expired");
        }

        return new UserInfoResponseDto(
                ((Number) payload.get("id")).longValue(),
                (String) payload.get("fullName"),
                (String) payload.get("email"),
                (String) payload.get("phone"),
                ((Number) payload.get("role_id")).intValue(),
                (String) payload.get("role_name")  // Thêm role_name vào

        );
    }

    @Override
    public void updateUserInfo(String authHeader, UpdateUserRequestDto request)  {
        String token = jwtHelper.extractToken(authHeader);
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
    public BaseResponse changePassword(String authHeader, ChangePasswordRequestDto request) {
        BaseResponse response = new BaseResponse();

        String token = jwtHelper.extractToken(authHeader);
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


}
