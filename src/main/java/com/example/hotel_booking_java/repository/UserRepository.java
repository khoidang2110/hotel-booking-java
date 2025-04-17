package com.example.hotel_booking_java.repository;

import com.example.hotel_booking_java.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {


    Optional<Users> findByEmail(String email);
    Optional<Users> findByPhone(String phone); // ➕ Thêm dòng này
}
