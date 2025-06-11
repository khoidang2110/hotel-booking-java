package com.example.hotel_booking_java.payload.response;



import lombok.Data;

@Data
public class BaseResponse {
    private int code;
    private String message;
    private Object data;
}
