package com.example.hotel_booking_java.services;

import com.example.hotel_booking_java.dto.BookingDTO;
import com.example.hotel_booking_java.payload.request.BookingRequest;

import java.util.List;

public interface BookingServices {
    BookingDTO createBooking(BookingRequest req, int userId);
    List<BookingDTO> getAll(int pageNumber, int pageSize);
    BookingDTO getOne(int id);
    BookingDTO updateBooking(int id, BookingRequest req, int userId);
    void deleteBooking(int id);
}
