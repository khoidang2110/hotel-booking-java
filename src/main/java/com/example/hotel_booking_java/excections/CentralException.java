package com.example.hotel_booking_java.excections;



import com.example.hotel_booking_java.payload.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CentralException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(Exception exception){
        BaseResponse response = new BaseResponse();
        response.setCode(500);
        response.setMessage(exception.getMessage());

        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleParameter(Exception exception){
        BaseResponse response = new BaseResponse();
        response.setCode(400);
        response.setMessage(exception.getMessage());

        return ResponseEntity.status(400).body(response);
    }
}
