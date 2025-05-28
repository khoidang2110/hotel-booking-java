package com.example.hotel_booking_java.mapper;

import com.example.hotel_booking_java.dto.payment.PaymentDetailDto;
import com.example.hotel_booking_java.dto.payment.PaymentDto;
import com.example.hotel_booking_java.dto.service.ServiceDto;
import com.example.hotel_booking_java.entity.PaymentDetail;
import com.example.hotel_booking_java.entity.Payments;

import java.util.ArrayList;
import java.util.List;


public class PaymentMapper {

    public static PaymentDto toDto(Payments payment) {
        PaymentDto dto = new PaymentDto();
        dto.setId(payment.getId());

        // Lấy bookingId từ entity
        if (payment.getBooking() != null) {
            dto.setBookingId(payment.getBooking().getId());
        }

        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setModifiedAt(payment.getModifiedAt());

        // mapping booking và createdBy
//        dto.setBooking(BookingMapper.toDto(payment.getBooking()));
//        dto.setCreatedBy(UserMapper.toDto(payment.getCreatedBy()));
        dto.setBooking(BookingMapper.toDto(payment.getBooking())); // ✅ thêm dòng này

        // mapping paymentDetails
        List<PaymentDetailDto> detailDtos = new ArrayList<>();
        if (payment.getPaymentDetails() != null) {
            for (PaymentDetail detail : payment.getPaymentDetails()) {
                PaymentDetailDto detailDto = new PaymentDetailDto();
                detailDto.setId(detail.getId());
                detailDto.setQuantity(detail.getQuantity());
                detailDto.setPrice(detail.getPrice());
                detailDto.setTotal(detail.getTotal());

                ServiceDto serviceDto = new ServiceDto();
                serviceDto.setId(detail.getService().getId());
                serviceDto.setName(detail.getService().getName());

                detailDto.setService(serviceDto);
                detailDtos.add(detailDto);
            }
        }
        dto.setPaymentDetails(detailDtos);

        return dto;
    }


}
