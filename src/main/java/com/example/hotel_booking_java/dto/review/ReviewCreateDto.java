package com.example.hotel_booking_java.dto.review;

import lombok.Data;

@Data
public class ReviewCreateDto {

    private int roomId;
    private int rating; // 1 đến 5
    private String comment;
}
