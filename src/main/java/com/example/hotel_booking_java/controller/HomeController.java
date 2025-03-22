package com.example.hotel_booking_java.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {
        return "<h1>Khoi update 22-03-2025 </h1>";
    }
}
