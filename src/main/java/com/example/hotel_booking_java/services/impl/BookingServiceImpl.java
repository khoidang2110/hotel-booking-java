package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.BookingDTO;
import com.example.hotel_booking_java.entity.Bookings;
import com.example.hotel_booking_java.entity.Rooms;
import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.enums.BookingEnum;
import com.example.hotel_booking_java.payload.request.BookingRequest;
import com.example.hotel_booking_java.repository.BookingRepo;
import com.example.hotel_booking_java.repository.RoomRepository;
import com.example.hotel_booking_java.services.BookingServices;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingServices {
    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private RoomRepository roomRepo;

    private BigDecimal calcTotal(Rooms room, BookingRequest r) {
        long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
        return room.getPrice().multiply(BigDecimal.valueOf(nights));
    }

    private BookingDTO toDto(Bookings b) {
        BookingDTO d = new BookingDTO();
        d.setId(b.getId());
        d.setRoom(b.getRoom());
        d.setUserId(b.getUser().getId());
        d.setCheckIn(b.getCheckIn());
        d.setCheckOut(b.getCheckOut());
        d.setStatus(b.getStatus());
        d.setTotalPrice(b.getTotalPrice());
        return d;
    }

    @Override
    @Transactional
    public BookingDTO createBooking(BookingRequest r, int userId) {

        Rooms room = roomRepo.findById(r.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Bookings b = new Bookings();
        b.setRoom(room);
        Users user = new Users();
        user.setId(userId); // set userId cho đối tượng User
        b.setUser(user);


        b.setCheckIn(r.getCheckIn());
        b.setCheckOut(r.getCheckOut());

        b.setCouponDiscountId(r.getCouponDiscountId());
        b.setSpecialDayDiscountId(r.getSpecialDayDiscountId());

        b.setStatus(r.getStatus() == null ? BookingEnum.PENDING : r.getStatus());

        BigDecimal price = r.getTotalPrice() != null ? r.getTotalPrice()
                : calcTotal(room, r);
        b.setTotalPrice(price);

        b.setCreatedBy(userId);
        b.setModifiedBy(userId);

        return toDto(bookingRepo.save(b));
    }

    @Override
    public List<BookingDTO> getAll(int page, int size) {
        return bookingRepo.findAll(PageRequest.of(page, size))
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public BookingDTO getOne(int id) {
        return toDto(bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found")));
    }

    @Override
    @Transactional
    public BookingDTO updateBooking(int id, BookingRequest r, int userId) {
        Bookings b = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (r.getRoomId() != null) {
            Rooms room = roomRepo.findById(r.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            b.setRoom(room);
            b.setTotalPrice(calcTotal(room, r));
        }
        if (r.getCheckIn() != null) b.setCheckIn(r.getCheckIn());
        if (r.getCheckOut() != null) b.setCheckOut(r.getCheckOut());
        b.setCouponDiscountId(r.getCouponDiscountId());
        b.setSpecialDayDiscountId(r.getSpecialDayDiscountId());
        b.setModifiedBy(userId);

        return toDto(bookingRepo.save(b));
    }

    @Override
    public void deleteBooking(int id) {
        bookingRepo.deleteById(id);
    }
}
