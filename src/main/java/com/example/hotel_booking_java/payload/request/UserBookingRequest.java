package com.example.hotel_booking_java.payload.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UserBookingRequest {
    @NotNull
    private Integer roomId;

    @NotNull(message = "check‑in date is required")
    @Future(message = "check‑in must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "MM/dd/yyyy")
    private LocalDate checkIn;

    @NotNull(message = "check‑in date is required")
    @Future(message = "check‑in must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "MM/dd/yyyy")
    private LocalDate checkOut;

    @NotNull(message = "Number of adults is required")
    @Min(value = 1, message = "There must be at least 1 adult")
    private int adultNumber;

    @NotNull(message = "Number of children is required")
    @Min(value = 0, message = "Number of children cannot be negative")
    private int childNumber;

    private String specialDayDiscountCode;

    private String couponDiscountCode;
}
