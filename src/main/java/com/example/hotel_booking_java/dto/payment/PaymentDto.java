package com.example.hotel_booking_java.dto.payment;

import com.example.hotel_booking_java.dto.BookingDTO;
import com.example.hotel_booking_java.enums.PaymentMethod;
import com.example.hotel_booking_java.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class PaymentDto {
    private int id;
    private int bookingId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int createdBy;
    private Integer modifiedBy;

    private BookingDTO booking; // ✅ Thêm dòng này

    private List<PaymentDetailDto> paymentDetails; // Thêm dòng này

}
