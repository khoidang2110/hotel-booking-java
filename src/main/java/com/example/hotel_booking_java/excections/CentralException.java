package com.example.hotel_booking_java.excections;



import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParseError(HttpMessageNotReadableException exception) {
        BaseResponse response = new BaseResponse();
        response.setCode(400);

        String message = "Invalid request format";

        if (exception.getCause() instanceof InvalidFormatException cause) {
            String field = cause.getPathReference(); // hoặc dùng `cause.getPath().get(0).getFieldName()` nếu cần chi tiết hơn
            message = "Invalid value for field " + field + ": " + cause.getValue();
        }

        response.setMessage(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
