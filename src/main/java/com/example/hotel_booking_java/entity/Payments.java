package com.example.hotel_booking_java.entity;

import com.example.hotel_booking_java.enums.PaymentMethod;
import com.example.hotel_booking_java.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.security.Timestamp;

@Entity
@Table(name = "payments")
@Data
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Bookings bookings;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;


    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private Timestamp modifiedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;
}
