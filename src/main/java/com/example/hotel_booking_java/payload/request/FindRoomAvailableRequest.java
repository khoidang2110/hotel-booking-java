package com.example.hotel_booking_java.payload.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class FindRoomAvailableRequest {

    @NotNull(message = "check‑in date is required")
    @Future(message = "check‑in must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "MM/dd/yyyy")
    private LocalDate checkIn;

    @NotNull(message = "check‑out date is required")
    @Future(message = "check‑out must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "MM/dd/yyyy")
    private LocalDate checkOut;

    private int adultNumber;

    private int childNumber;
}
