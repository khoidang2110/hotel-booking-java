package com.example.hotel_booking_java.dto;

import com.example.hotel_booking_java.entity.Rooms;
import com.example.hotel_booking_java.enums.BookingEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Integer id;
    private Integer userId;
    private Rooms room;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private BookingEnum status = BookingEnum.PENDING;
    private BigDecimal totalPrice;
    private Integer couponDiscountId;
    private Integer specialDayDiscountId;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private Integer createdBy;
    private Integer modifiedBy;
}
