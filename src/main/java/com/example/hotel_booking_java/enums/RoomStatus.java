package com.example.hotel_booking_java.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomStatus {
    AVAILABLE,
    BOOKED,
    OCCUPIED,
    MAINTENANCE;
//
//    @JsonValue
//    public String toLowerCase() {
//        return this.name().toLowerCase();
//    }
}

