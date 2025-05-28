package com.example.hotel_booking_java.mapper;

import com.example.hotel_booking_java.dto.BookingDTO;
import com.example.hotel_booking_java.dto.user.UserInfoResponseDto;
import com.example.hotel_booking_java.entity.Bookings;
import com.example.hotel_booking_java.entity.Users;

import java.sql.Timestamp;

public class BookingMapper {

    public static BookingDTO toDto(Bookings booking) {
        if (booking == null) return null;

        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());

        Users user = booking.getUser();
        dto.setUserId(user.getId());

        dto.setUser(new UserInfoResponseDto(
                (long) user.getId(),  // ép kiểu int sang long rồi tự động boxing thành Long
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().getId(),
                user.getRole().getName().name()
        ));


        // mapping đơn giản
        dto.setUserId(booking.getUser().getId()); // chỉ lấy id
        dto.setRoom(booking.getRoom()); // full room object

        dto.setCheckIn(booking.getCheckIn());
        dto.setCheckOut(booking.getCheckOut());
        dto.setStatus(booking.getStatus());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setCouponDiscountId(booking.getCouponDiscountId());
        dto.setSpecialDayDiscountId(booking.getSpecialDayDiscountId());

//        // convert LocalDateTime -> java.sql.Timestamp
//        if (booking.getCreatedAt() != null) {
//            dto.setCreatedAt(Timestamp.valueOf(booking.getCreatedAt()));
//        }
//
//        if (booking.getModifiedAt() != null) {
//            dto.setModifiedAt(Timestamp.valueOf(booking.getModifiedAt()));
//        }

        dto.setCreatedBy(booking.getCreatedBy());
        dto.setModifiedBy(booking.getModifiedBy());

        return dto;
    }
}
