package com.example.hotel_booking_java.payload.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotNull(message="Fullname is not null")
    private String fullName;

    @NotNull(message="Password is not null")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String password;


    @NotNull(message="Email is not null")
    @NotEmpty(message="Email is not empty")
    // @Pattern(regexp="[a-zA-Z][a-zA-Z ]")
    @Email(message="wrong email format")
    private String email;

    @NotNull(message="Phone is not null")
    @Pattern(regexp = "\\d{10,}", message = "Phone must have at least 10 digits")
    private String phone;
}

