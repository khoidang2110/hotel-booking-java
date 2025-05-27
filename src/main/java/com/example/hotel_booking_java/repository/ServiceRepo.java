package com.example.hotel_booking_java.repository;

import com.example.hotel_booking_java.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepo extends JpaRepository<Services, Integer> {

}
