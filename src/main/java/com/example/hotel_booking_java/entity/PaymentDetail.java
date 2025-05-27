package com.example.hotel_booking_java.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "payment_detail")
@Data
public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    @JsonBackReference // Ngăn Jackson serialize lại payment
    private Payments payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Services service;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;
    @Column(name = "total", precision = 15, scale = 2)
    private BigDecimal total;


}


