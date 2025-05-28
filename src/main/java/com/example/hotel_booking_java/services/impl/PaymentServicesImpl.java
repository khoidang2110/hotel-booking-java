package com.example.hotel_booking_java.services.impl;

import com.example.hotel_booking_java.dto.payment.*;
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
import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public void addServiceToPayment(String authHeader,AddServiceToPaymentDto request) {
        // Kiểm tra quyền người dùng từ JWT
        System.out.println("run add service task " );

        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        Payments payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + request.getPaymentId()));

        Services service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + request.getServiceId()));

        BigDecimal lineTotal = service.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        PaymentDetail detail = new PaymentDetail();
        detail.setPayment(payment);
        detail.setService(service);
        detail.setQuantity(request.getQuantity());
        detail.setPrice(service.getPrice());
        detail.setTotal(lineTotal);

        paymentDetailRepository.save(detail);

        // Cập nhật tổng tiền mới cho payment
        BigDecimal newAmount = payment.getAmount().add(lineTotal);
        payment.setAmount(newAmount);
        payment.setModifiedAt(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void removeServiceFromPayment(String authHeader, int paymentDetailId) {
        // Kiểm tra quyền người dùng từ JWT
        UserPermissionDto permission = jwtHelper.getUserPermission(authHeader);
        if (!permission.isHasPermission()) {
            throw new RuntimeException("Permission denied");
        }

        // Tìm chi tiết thanh toán để xoá
        PaymentDetail paymentDetail = paymentDetailRepository.findById(paymentDetailId)
                .orElseThrow(() -> new RuntimeException("Payment detail not found with id: " + paymentDetailId));

        // Lấy số tiền của chi tiết thanh toán này để giảm tổng số tiền của thanh toán chính
        BigDecimal lineTotal = paymentDetail.getTotal();

        // Tìm thanh toán chính
        Payments payment = paymentDetail.getPayment();

        // Xoá chi tiết thanh toán khỏi danh sách chi tiết của thanh toán chính
        payment.getPaymentDetails().remove(paymentDetail);

        // Cập nhật tổng số tiền mới cho thanh toán chính
        BigDecimal newAmount = payment.getAmount().subtract(lineTotal);
        payment.setAmount(newAmount);
        payment.setModifiedAt(LocalDateTime.now());

        // Lưu lại các thay đổi
        paymentRepository.save(payment);
        paymentDetailRepository.delete(paymentDetail);
    }

}
