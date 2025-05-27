package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.dto.payment.CreatePaymentDto;
import com.example.hotel_booking_java.dto.payment.UpdatePaymentDto;
import com.example.hotel_booking_java.dto.service.CreateServiceDto;
import com.example.hotel_booking_java.dto.service.UpdateServiceDto;
import com.example.hotel_booking_java.entity.Payments;
import com.example.hotel_booking_java.entity.Services;

import java.util.List;

public interface ServiceServices {

    void createService(String authHeader, CreateServiceDto request);

    void updateService(String authHeader, UpdateServiceDto request);
    void deleteService(String authHeader,int id);
    Services getServiceById(String authHeader, int serviceId) ;
    List<Services> getAllServices(String authHeader, int page, int size);

}
