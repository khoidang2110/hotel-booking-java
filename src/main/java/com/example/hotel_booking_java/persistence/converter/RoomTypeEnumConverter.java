package com.example.hotel_booking_java.persistence.converter;

import com.example.hotel_booking_java.enums.RoomType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoomTypeEnumConverter implements AttributeConverter<RoomType, String> {
    @Override
    public String convertToDatabaseColumn(RoomType roomType) {
        return (roomType == null) ? null : roomType.name().toLowerCase();

    }

    @Override
    public RoomType convertToEntityAttribute(String s) {
        return (s == null) ? null : RoomType.valueOf(s.toUpperCase());

    }
}
