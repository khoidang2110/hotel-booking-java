package com.example.hotel_booking_java.persistence.converter;

import com.example.hotel_booking_java.enums.BookingEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookingEnumConverter implements AttributeConverter<BookingEnum, String> {
    @Override
    public String convertToDatabaseColumn(BookingEnum bookingEnum) {
        return (bookingEnum == null) ? null : bookingEnum.name().toLowerCase();

    }

    @Override
    public BookingEnum convertToEntityAttribute(String s) {
        return (s == null) ? null : BookingEnum.valueOf(s.toUpperCase());

    }
}
