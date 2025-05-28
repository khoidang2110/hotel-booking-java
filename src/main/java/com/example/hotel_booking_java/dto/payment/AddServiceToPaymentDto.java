package com.example.hotel_booking_java.dto.payment;

import lombok.Data;

@Data
public class AddServiceToPaymentDto {
    private int paymentId;
    private int serviceId;
    private int quantity;
}
