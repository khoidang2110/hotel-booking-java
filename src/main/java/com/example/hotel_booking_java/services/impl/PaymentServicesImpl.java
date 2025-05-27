package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.payment.CreatePaymentDto;
import com.example.hotel_booking_java.dto.payment.PaymentDto;
import com.example.hotel_booking_java.dto.payment.PaymentServiceItemDto;
import com.example.hotel_booking_java.dto.payment.UpdatePaymentDto;
import com.example.hotel_booking_java.dto.user.UserPermissionDto;
import com.example.hotel_booking_java.entity.*;
import com.example.hotel_booking_java.mapper.PaymentMapper;
import com.example.hotel_booking_java.repository.*;
import com.example.hotel_booking_java.services.PaymentServices;
import com.example.hotel_booking_java.utils.JwtHelper;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServicesImpl implements PaymentServices {

    @Autowired
    private  PaymentRepository paymentRepository;

    @Autowired
    private BookingRepo bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private ServiceRepo serviceRepository;

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

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

//    @Override
//    public void createPayment(String authHeader, CreatePaymentDto request) {
//
//
//
//
//
//        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
//
//        if (permission.isHasPermission()) {
//            // Người dùng có quyền, xử lý tiếp theo
//            int userId = permission.getUserId();
//            // Tiến hành xử lý với userId
//
//            if (paymentRepository.existsByBooking_Id(request.getBookingId())) {
//                throw new RuntimeException("Payment already exists for this booking_id: " + request.getBookingId());
//            }
//
//            try {
//                Bookings booking = bookingRepository.findById(request.getBookingId())
//                        .orElseThrow(() -> new RuntimeException("Booking not found with id: " + request.getBookingId()));
//
//                Users user = userRepository.findById(userId)
//                        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//
//                Payments payment = new Payments();
//                payment.setBooking(booking);
//                payment.setAmount(request.getAmount());
//                payment.setPaymentMethod(request.getPaymentMethod());
//                payment.setStatus(request.getStatus());
//                // payment.setCreatedBy(Integer.parseInt(payload.get("id").toString()));
//                payment.setCreatedBy(user);
//
//                paymentRepository.save(payment);
//            } catch (IllegalArgumentException e) {
//                throw new RuntimeException("Invalid payment method or status", e);
//            }
//
//        } else {
//
//            throw new RuntimeException("Permission denied");
//        }
//
//
//    }
//


    @Override
    @Transactional
    public void createPayment(String authHeader, CreatePaymentDto request) {
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);

        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        int userId = permission.getUserId();

        if (paymentRepository.existsByBooking_Id(request.getBookingId())) {
            throw new RuntimeException("Payment already exists for booking_id: " + request.getBookingId());
        }

        try {
            Bookings booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new RuntimeException("Booking not found with id: " + request.getBookingId()));

            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            // Tính tổng tiền từ dịch vụ nếu có
            BigDecimal serviceTotal = BigDecimal.ZERO;
            List<PaymentDetail> paymentDetails = new ArrayList<>();

            if (request.getServices() != null && !request.getServices().isEmpty()) {
                for (PaymentServiceItemDto item : request.getServices()) {
                    Services service = serviceRepository.findById(item.getServiceId())
                            .orElseThrow(() -> new RuntimeException("Service not found with id: " + item.getServiceId()));

                    BigDecimal lineTotal = service.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    serviceTotal = serviceTotal.add(lineTotal);

                    PaymentDetail detail = new PaymentDetail();
                    detail.setService(service);
                    detail.setQuantity(item.getQuantity());
                    detail.setPrice(service.getPrice());
                    detail.setTotal(lineTotal);
                    paymentDetails.add(detail);
                }
            }

            BigDecimal totalAmount = booking.getTotalPrice().add(serviceTotal);

            // Tạo payment
            Payments payment = new Payments();
            payment.setBooking(booking);
            payment.setAmount(totalAmount);
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setStatus(request.getStatus());
            payment.setCreatedBy(user);

            // Lưu payment trước để có ID
            payment = paymentRepository.save(payment);

            // Liên kết paymentDetails với payment
            for (PaymentDetail detail : paymentDetails) {
                detail.setPayment(payment);
            }

            paymentDetailRepository.saveAll(paymentDetails);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid payment method or status", e);
        }
    }

    @Override
    public PaymentDto getPaymentById(String authHeader, int id) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        // Fetch payment and convert to DTO
        Payments payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment with ID " + id + " not found"));

        return PaymentMapper.toDto(payment);
    }

    @Override
    public List<PaymentDto> getAllPayments(String authHeader, int page, int size) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        // Fetch payments with pagination
        List<Payments> payments = paymentRepository.findAll(PageRequest.of(page, size)).getContent();

        // Map payments to DTO
        return payments.stream()
                .map(PaymentMapper::toDto)  // Dùng mapper để chuyển entity thành DTO
                .collect(Collectors.toList());
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
