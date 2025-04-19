package com.example.hotel_booking_java.repository;


import com.example.hotel_booking_java.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Rooms, Integer> {
    @Query("""
            SELECT r FROM Rooms r
            WHERE r.status = com.example.hotel_booking_java.enums.RoomStatus.AVAILABLE
              AND r.id NOT IN (
                SELECT b.room.id FROM Bookings b
                WHERE b.checkIn  < :checkOut
                  AND b.checkOut > :checkIn
                  AND b.status   <> com.example.hotel_booking_java.enums.BookingEnum.CANCELLED
            )
            """)
    List<Rooms> findAvailableRooms(@Param("checkIn") LocalDate checkIn,
                                   @Param("checkOut") LocalDate checkOut);
}
