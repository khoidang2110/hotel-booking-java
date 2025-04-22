package com.example.hotel_booking_java.dto.user;


import com.example.hotel_booking_java.entity.Roles;
import com.example.hotel_booking_java.entity.Users;
import lombok.Data;


@Data
public class UserInfoResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private RoleDTO role;

    // Constructor sửa lại để nhận role_id và role_name trực tiếp từ payload
    public UserInfoResponseDto(Long id, String fullName, String email, String phone, int roleId, String roleName) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = new RoleDTO(roleId, roleName);  // Truyền role_id và role_name vào đây
    }

    @Data
    public static class RoleDTO {
        private int id;
        private String name;

        // Constructor của RoleDTO sẽ nhận role_id và role_name
        public RoleDTO(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}