package com.example.hotel_booking_java.dto.payment;

import com.example.hotel_booking_java.dto.service.ServiceDto;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class PaymentDetailDto {
    private int id;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;
    private ServiceDto service;
}
