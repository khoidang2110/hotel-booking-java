package com.example.hotel_booking_java.payload.request;

import com.example.hotel_booking_java.enums.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomRequest {
    @NotBlank(message = "Room number must not be blank")
    private String roomNumber;

    @NotNull(message = "Room type must not be null")
    private RoomType type;

    @NotNull(message = "Price must not be null")
    private BigDecimal price;

    private String description;
}
