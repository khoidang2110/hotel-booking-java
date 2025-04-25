package com.example.hotel_booking_java.controller;




import com.example.hotel_booking_java.dto.user.LoginRequestDto;
import com.example.hotel_booking_java.dto.user.LoginResponseDto;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.services.AuthenticationServices;
import com.example.hotel_booking_java.services.LoginAttemptService;
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

    @Autowired
    private LoginAttemptService loginAttemptService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        BaseResponse response = new BaseResponse();
        String email = request.getEmail();

        if (loginAttemptService.isBlocked(email)) {
            response.setMessage("Bạn đã nhập sai quá 3 lần. Vui lòng thử lại sau vài phút.");
            return ResponseEntity.status(403).body(response);
        }

        try {

            LoginResponseDto loginResponseDto = authenticationServices.login(request); // ✅
            loginAttemptService.loginSucceeded(email); // Đăng nhập đúng thì reset

            response.setMessage("Login successful");
            response.setData(loginResponseDto); // ✅ set cả token và username
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            loginAttemptService.loginFailed(email); // Ghi nhận đăng nhập sai

            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

