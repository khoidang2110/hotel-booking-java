package com.example.hotel_booking_java;

import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelBookingJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelBookingJavaApplication.class, args);
    }

}
