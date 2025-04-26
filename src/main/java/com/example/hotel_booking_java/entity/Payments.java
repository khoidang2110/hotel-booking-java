package com.example.hotel_booking_java.entity;


import com.example.hotel_booking_java.entity.Bookings;
import com.example.hotel_booking_java.entity.Users;

import com.example.hotel_booking_java.enums.PaymentMethod;
import com.example.hotel_booking_java.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity(name = "payments")
//@Table(name = "payments")
@Data
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    //@Convert(converter = PaymentMethodEnumConverter.class)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod = PaymentMethod.CASH;

    @Enumerated(EnumType.STRING)
    // @Convert(converter = PaymentStatusEnumConverter.class)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();


    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt = LocalDateTime.now();




//    @Column(name = "created_by", nullable = false)
//    private int createdBy;



    @Column(name = "modified_by")
    private Integer modifiedBy;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Bookings booking;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Users createdBy;
}
