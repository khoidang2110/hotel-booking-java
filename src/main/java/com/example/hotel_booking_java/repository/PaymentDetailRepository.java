package com.example.hotel_booking_java.repository;

import com.example.hotel_booking_java.entity.PaymentDetail;
import com.example.hotel_booking_java.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {
}
