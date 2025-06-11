package com.example.hotel_booking_java.persistence.converter;

import com.example.hotel_booking_java.enums.BookingEnum;
import com.example.hotel_booking_java.enums.DiscountType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DiscountEnumConverter implements AttributeConverter<DiscountType, String> {
    @Override
    public String convertToDatabaseColumn(DiscountType discountType) {
        return (discountType == null) ? null : discountType.name().toLowerCase();

    }

    @Override
    public DiscountType convertToEntityAttribute(String s) {
        return (s == null) ? null : DiscountType.valueOf(s.toUpperCase());

    }
}
