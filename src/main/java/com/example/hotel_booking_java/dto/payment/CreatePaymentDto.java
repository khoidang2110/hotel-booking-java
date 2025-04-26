package com.example.hotel_booking_java.dto.payment;

import com.example.hotel_booking_java.enums.PaymentMethod;
import com.example.hotel_booking_java.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentDto {
    private int bookingId;
    private BigDecimal amount;

 private PaymentMethod paymentMethod;
 private PaymentStatus status;

}
