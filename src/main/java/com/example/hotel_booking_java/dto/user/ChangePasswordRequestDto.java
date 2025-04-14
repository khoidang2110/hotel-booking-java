package com.example.hotel_booking_java.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequestDto {

    @NotNull(message="Old password must not be blank")
    private String oldPassword;

    @NotNull(message="New password must not be blank")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String newPassword;
}
