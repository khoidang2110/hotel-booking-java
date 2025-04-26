package com.example.hotel_booking_java.entity;


import com.example.hotel_booking_java.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name="name", nullable = false)
    private RoleEnum name;


}
