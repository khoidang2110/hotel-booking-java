package com.example.hotel_booking_java.dto.service;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateServiceDto {
    private String name;
    private BigDecimal price;


}
