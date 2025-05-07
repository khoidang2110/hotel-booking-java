package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.BookingDTO;
import com.example.hotel_booking_java.dto.payment.CreatePaymentDto;
import com.example.hotel_booking_java.dto.payment.PaymentDto;
import com.example.hotel_booking_java.dto.payment.UpdatePaymentDto;
import com.example.hotel_booking_java.dto.user.UserPermissionDto;
import com.example.hotel_booking_java.entity.Bookings;
import com.example.hotel_booking_java.entity.Payments;
import com.example.hotel_booking_java.entity.Users;
import com.example.hotel_booking_java.enums.PaymentMethod;
import com.example.hotel_booking_java.enums.PaymentStatus;
import com.example.hotel_booking_java.payload.response.BaseResponse;
import com.example.hotel_booking_java.repository.BookingRepo;
import com.example.hotel_booking_java.repository.PaymentRepository;
import com.example.hotel_booking_java.repository.UserRepository;
import com.example.hotel_booking_java.services.PaymentService;
import com.example.hotel_booking_java.utils.JwtHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private  PaymentRepository paymentRepository;

    @Autowired
    private BookingRepo bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;


    public PaymentDto convertToDto(Payments payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(payment.getId());
      //  paymentDto.setBookingId(payment.getBookingId());
        paymentDto.setBookingId(payment.getBooking().getId());

        paymentDto.setAmount(payment.getAmount());
        paymentDto.setPaymentMethod(payment.getPaymentMethod());
        paymentDto.setStatus(payment.getStatus());
        paymentDto.setCreatedAt(payment.getCreatedAt());
        paymentDto.setModifiedAt(payment.getModifiedAt());
        paymentDto.setCreatedBy(payment.getCreatedBy().getId());
        paymentDto.setModifiedBy(payment.getModifiedBy());

        return paymentDto;
    }
    @Override
    public void createPayment(String authHeader, CreatePaymentDto request) {





        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);

        if (permission.isHasPermission()) {
            // Người dùng có quyền, xử lý tiếp theo
            int userId = permission.getUserId();
            // Tiến hành xử lý với userId

            if (paymentRepository.existsByBooking_Id(request.getBookingId())) {
                throw new RuntimeException("Payment already exists for this booking_id: " + request.getBookingId());
            }

            try {
                Bookings booking = bookingRepository.findById(request.getBookingId())
                        .orElseThrow(() -> new RuntimeException("Booking not found with id: " + request.getBookingId()));

                Users user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

                Payments payment = new Payments();
                payment.setBooking(booking);
                payment.setAmount(request.getAmount());
                payment.setPaymentMethod(request.getPaymentMethod());
                payment.setStatus(request.getStatus());
                // payment.setCreatedBy(Integer.parseInt(payload.get("id").toString()));
                payment.setCreatedBy(user);

                paymentRepository.save(payment);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid payment method or status", e);
            }

        } else {

            throw new RuntimeException("Permission denied");
        }


    }


    @Override
    public Payments getPaymentById(String authHeader, int id) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        // Fetch payment and handle possible absence
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment with ID " + id + " not found"));
    }

    @Override
    public List<Payments> getAllPayments(String authHeader, int page, int size) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        // Fetch payments with pagination
        return paymentRepository.findAll(PageRequest.of(page, size)).getContent();
    }


    @Override
    @Transactional
    public void updatePayment(String authHeader, UpdatePaymentDto request) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }
        // Kiểm tra và lấy thông tin user từ authHeader nếu cần
        Payments payment = paymentRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Cập nhật các trường mới từ UpdatePaymentDto
        if (request.getAmount() != null) {
            payment.setAmount(request.getAmount());
        }
        if (request.getStatus() != null) {
            payment.setStatus(request.getStatus());
        }
        if (request.getPaymentMethod() != null) {
            payment.setPaymentMethod(request.getPaymentMethod());
        }

        paymentRepository.save(payment); // Lưu lại bản cập nhật
    }


    @Override
    public void deletePayment(String authHeader, int id) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("payment not found with id " + id);
        }
        paymentRepository.deleteById(id);
    }


}
