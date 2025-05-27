package com.example.hotel_booking_java.dto.payment;

import com.example.hotel_booking_java.enums.PaymentMethod;
import com.example.hotel_booking_java.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreatePaymentDto {
    private int bookingId;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private List<PaymentServiceItemDto> services; // thêm danh sách dịch vụ
}

