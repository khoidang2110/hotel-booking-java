package com.example.hotel_booking_java.repository;

import com.example.hotel_booking_java.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends JpaRepository<Bookings, Integer> {

}
