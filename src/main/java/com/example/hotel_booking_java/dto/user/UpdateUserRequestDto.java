package com.example.hotel_booking_java.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRequestDto {

    @NotEmpty(message="Email is not empty")
    @NotNull(message="Email is not null")
    @Email(message="Wrong email format")
    private String email;


    private String fullName;

    @Pattern(regexp = "\\d{10,}", message = "Phone must have at least 10 digits")
    private String phone;


    // Getters + Setters
}
