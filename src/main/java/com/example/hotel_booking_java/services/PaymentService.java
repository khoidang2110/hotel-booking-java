package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.dto.payment.CreatePaymentDto;
import com.example.hotel_booking_java.dto.payment.PaymentDto;
import com.example.hotel_booking_java.dto.payment.UpdatePaymentDto;
import com.example.hotel_booking_java.entity.Payments;

import java.util.List;

public interface PaymentService {
    void createPayment(String authHeader, CreatePaymentDto request);
    Payments getPaymentById(String authHeader, int paymentId) ;
  List<Payments> getAllPayments(String authHeader, int page, int size);

    void updatePayment(String authHeader, UpdatePaymentDto request);
}
