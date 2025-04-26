package com.example.hotel_booking_java.repository;

import com.example.hotel_booking_java.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payments, Integer> {
    // có thể thêm custom query nếu cần
  //  boolean existsByBookingId(int bookingId);  // Method to check if the booking_id exists
    boolean existsByBooking_Id(int bookingId);

}
