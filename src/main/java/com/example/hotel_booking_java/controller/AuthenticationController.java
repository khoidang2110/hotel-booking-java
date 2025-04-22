package com.example.hotel_booking_java.controller;




import com.example.hotel_booking_java.dto.user.LoginRequestDto;
import com.example.hotel_booking_java.dto.user.LoginResponseDto;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.services.AuthenticationServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * {
 *     "code":200,
 *     "message":"",
 *     "data":""
 * }
 */






@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationServices authenticationServices;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        BaseResponse response = new BaseResponse();
        try {
//            String token = authenticationServices.login(request);
//            response.setMessage("Login successful");
//            response.setData(token);
//            return ResponseEntity.ok(response);
            LoginResponseDto loginResponseDto = authenticationServices.login(request); // ✅
            response.setMessage("Login successful");
            response.setData(loginResponseDto); // ✅ set cả token và username
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

