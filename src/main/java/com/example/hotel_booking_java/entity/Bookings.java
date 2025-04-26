package com.example.hotel_booking_java.entity;




import com.example.hotel_booking_java.enums.BookingEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // For demonstration, we use a simple integer for user reference.
//    @Column(name = "user_id", nullable = false)
//    private Integer userId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    // Many bookings can be associated with one room.
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms room;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    //@Convert(converter = BookingEnumConverter.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingEnum status = BookingEnum.PENDING;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    // Discount foreign keys are represented as integers here (or, later, you can map to a Discount entity).
    @Column(name = "coupon_discount_id")
    private Integer couponDiscountId;

    @Column(name = "special_day_discount_id")
    private Integer specialDayDiscountId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;
}
