package com.example.hotel_booking_java.controller;



import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.payload.request.SignUpRequest;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.payload.response.UserInfoResponse;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.UserServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {


    @Autowired
    private UserServices userServices;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request){

        BaseResponse response = new BaseResponse();
        try {
            userServices.signUp(request);
            response.setMessage("User registered successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }



    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        BaseResponse response = new BaseResponse();

        try {
            // Lấy token từ header
            String token = authHeader.replace("Bearer ", "");

            // Giải mã token để lấy email
            String email = jwtHelper.decodeToken(token);

            // Tìm user theo email
            Users user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Tạo DTO để trả về, không chứa password
            UserInfoResponse userInfo = new UserInfoResponse(
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone()
            );
            response.setData(userInfo);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setMessage("Invalid token: " + e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

}
