package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.dto.payment.AddServiceToPaymentDto;
import com.example.hotel_booking_java.dto.payment.CreatePaymentDto;
import com.example.hotel_booking_java.dto.payment.PaymentDto;
import com.example.hotel_booking_java.dto.payment.UpdatePaymentDto;
import com.example.hotel_booking_java.entity.Payments;

import java.util.List;

public interface PaymentServices {
    PaymentDto getPaymentById(String authHeader, int paymentId) ;
  List<PaymentDto> getAllPayments(String authHeader, int page, int size);
    void createPayment(String authHeader, CreatePaymentDto request);
     void addServiceToPayment(String authHeader, AddServiceToPaymentDto request);
    void updatePayment(String authHeader, UpdatePaymentDto request);
    void deletePayment(String authHeader,int id);
    void removeServiceFromPayment(String authHeader, int paymentDetailId);
}
