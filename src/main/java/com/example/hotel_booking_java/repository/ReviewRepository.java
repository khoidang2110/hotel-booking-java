package com.example.hotel_booking_java.repository;

import com.example.hotel_booking_java.entity.Reviews;
import com.example.hotel_booking_java.entity.Rooms;
import com.example.hotel_booking_java.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Integer> {
  boolean existsByUserAndRoom(Users user, Rooms room);
    Reviews findByUserAndRoom(Users user, Rooms room);

}
