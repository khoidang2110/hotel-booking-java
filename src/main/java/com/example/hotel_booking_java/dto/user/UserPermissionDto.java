package com.example.hotel_booking_java.dto.user;

import lombok.Data;

@Data
public class UserPermissionDto {
    private boolean hasPermission;
    private int userId;

}
