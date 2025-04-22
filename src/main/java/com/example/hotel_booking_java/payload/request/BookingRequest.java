package com.example.hotel_booking_java.payload.request;

import com.example.hotel_booking_java.enums.BookingEnum;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @NotNull(message = "roomId must not be null")
    private Integer roomId;

    @NotNull(message = "check‑in date is required")
    @Future(message = "check‑in must be in the future")
    private LocalDateTime checkIn;

    @NotNull(message = "check‑out date is required")
    @Future(message = "check‑out must be in the future")
    private LocalDateTime checkOut;

    private BookingEnum status;

    private BigDecimal totalPrice;

    private Integer couponDiscountId;
    private Integer specialDayDiscountId;

    @AssertTrue(message = "check‑out must be **after** check‑in")
    private boolean isCheckoutAfterCheckin() {
        return checkIn == null || checkOut == null || checkOut.isAfter(checkIn);
    }

}
