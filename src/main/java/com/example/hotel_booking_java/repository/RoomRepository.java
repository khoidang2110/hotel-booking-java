package com.example.hotel_booking_java.repository;

import com.example.hotel_booking_java.entity.Reviews;
import com.example.hotel_booking_java.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository  extends JpaRepository<Rooms, Integer> {

}
