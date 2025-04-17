package com.example.hotel_booking_java.controller;



import com.example.hotel_booking_java.dto.user.ChangePasswordRequestDto;
import com.example.hotel_booking_java.dto.user.SignUpRequestDto;
import com.example.hotel_booking_java.dto.user.UpdateUserRequestDto;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.dto.user.UserInfoResponseDto;
import com.example.hotel_booking_java.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {


    @Autowired
    private UserServices userServices;

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JwtHelper jwtHelper;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDto request){

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
            UserInfoResponseDto userInfo = userServices.getUserInfoFromToken(authHeader);

            response.setData(userInfo);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setMessage("Invalid token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo( @RequestHeader("Authorization") String authHeader,
                                             @Valid @RequestBody UpdateUserRequestDto request) {
        BaseResponse response = new BaseResponse();

        try {
            userServices.updateUserInfo(authHeader, request);
            response.setMessage("User updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setMessage("Update failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String authHeader,
                                            @Valid @RequestBody ChangePasswordRequestDto request) {
        try {
            BaseResponse response = userServices.changePassword(authHeader, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            BaseResponse response = new BaseResponse();
            response.setMessage("Change password failed: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

}
