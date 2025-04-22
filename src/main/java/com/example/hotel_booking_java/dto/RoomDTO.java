package com.example.hotel_booking_java.dto;

import com.example.hotel_booking_java.enums.RoomStatus;
import com.example.hotel_booking_java.enums.RoomType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RoomDTO {
    private Integer id;
    private String roomNumber;
    private RoomType type;
    private BigDecimal price;
    private RoomStatus status = RoomStatus.AVAILABLE;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer createdBy;
    private Integer modifiedBy;
}
