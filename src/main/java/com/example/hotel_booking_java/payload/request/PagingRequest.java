package com.example.hotel_booking_java.payload.request;


import lombok.Data;

@Data
public class PagingRequest {
    private int page = 0;
    private int size = 10;
}
