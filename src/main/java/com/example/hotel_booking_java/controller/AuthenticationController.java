package com.example.hotel_booking_java.controller;




import com.example.hotel_booking_java.payload.request.LoginRequest;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.services.AuthenticationServices;
import com.example.hotel_booking_java.services.UserServices;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        BaseResponse response = new BaseResponse();
        try {
            String token = authenticationServices.login(request);
            response.setMessage("Login successful");
            response.setData(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

