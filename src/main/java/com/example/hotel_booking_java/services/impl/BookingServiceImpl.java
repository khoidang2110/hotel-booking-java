package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.BookingDTO;
import com.example.hotel_booking_java.entity.Bookings;
import com.example.hotel_booking_java.entity.Rooms;
import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.enums.BookingEnum;
import com.example.hotel_booking_java.payload.request.BookingRequest;
import com.example.hotel_booking_java.payload.request.GuestBookingRequest;
import com.example.hotel_booking_java.payload.request.UserBookingRequest;
import com.example.hotel_booking_java.repository.BookingRepo;
import com.example.hotel_booking_java.repository.RoomRepository;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.BookingServices;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingServices {
    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private RoomRepository roomsRepo;

    @Autowired
    private RoomRepository roomRepo;

    private BigDecimal calcTotal(Rooms room, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
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
                : calcTotal(room, r.getCheckIn(), r.getCheckOut());
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
            b.setTotalPrice(calcTotal(room, r.getCheckIn(), r.getCheckOut()));
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

    @Override
    @Transactional
    public BookingDTO createGuestBooking(GuestBookingRequest req) {
        // 1) Create a “guest” user
        Users guest = new Users();
        guest.setFullName(req.getGuestName());
        guest.setEmail(req.getGuestEmail());
        guest.setPhone(req.getGuestPhoneNumber());
        guest.setPassword("123456"); // Default password for guest, should be hashed in production
        guest.setRoleId(1); // or a dedicated GUEST_ROLE_ID constant
        Users savedGuest = usersRepo.save(guest);

        // 2) Lookup the room
        Rooms room = roomsRepo.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 3) Build the booking
        Bookings booking = new Bookings();
        booking.setUser(savedGuest);                      // ← use the persisted user
        booking.setRoom(room);                            // ← set the room
        booking.setCheckIn(req.getCheckIn());
        booking.setCheckOut(req.getCheckOut());
        booking.setStatus(BookingEnum.PENDING);
        booking.setTotalPrice(calcTotal(room, req.getCheckIn(), req.getCheckOut()));
        booking.setCouponDiscountId(null);
        booking.setSpecialDayDiscountId(null);
        booking.setCreatedBy(savedGuest.getId());         // ← creator is the guest
        booking.setModifiedBy(savedGuest.getId());

        bookingRepo.save(booking);
        return toDto(booking);
    }

    @Override
    public BookingDTO createUserBooking(UserBookingRequest req, Integer userId) {
        // 1) Find the existing user by phone
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String phone = user.getPhone();  // ← here’s the phone


        // 2) Lookup the room
        Rooms room = roomsRepo.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 3) Build and save the booking
        Bookings booking = new Bookings();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckIn(req.getCheckIn());
        booking.setCheckOut(req.getCheckOut());
        booking.setStatus(BookingEnum.PENDING);
        booking.setTotalPrice(calcTotal(room, req.getCheckIn(), req.getCheckOut()));

        // If you have coupon/special‐day codes, look them up here and set the IDs:
        booking.setCouponDiscountId(null);
        booking.setSpecialDayDiscountId(null);

        booking.setCreatedBy(user.getId());
        booking.setModifiedBy(user.getId());

        bookingRepo.save(booking);
        return toDto(booking);
    }
}
