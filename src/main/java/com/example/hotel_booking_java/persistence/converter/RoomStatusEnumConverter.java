package com.example.hotel_booking_java.persistence.converter;

import com.example.hotel_booking_java.enums.RoomStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoomStatusEnumConverter implements AttributeConverter<RoomStatus, String> {
    @Override
    public String convertToDatabaseColumn(RoomStatus roomStatus) {
        return (roomStatus == null) ? null : roomStatus.name().toLowerCase();

    }

    @Override
    public RoomStatus convertToEntityAttribute(String s) {
        return (s == null) ? null : RoomStatus.valueOf(s.toUpperCase());

    }
}
