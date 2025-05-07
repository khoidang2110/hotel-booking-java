package com.example.hotel_booking_java.utils;


import com.example.hotel_booking_java.dto.user.UserPermissionDto;
import com.example.hotel_booking_java.entity.Users;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String secret;
    private final long expiration = 86400000; // 1 day in milliseconds


    public String generateToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("fullName", user.getFullName());
        claims.put("email", user.getEmail());
        claims.put("phone", user.getPhone());
        claims.put("role_id", user.getRoleId());
        claims.put("role_name", user.getRole().getName());  // Lấy role_name từ đối tượng Roles

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();


    }
    public Map<String, Object> decodeToken (String token){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (JwtException e) {
            System.out.println("Decode error");
        }

        return null;
    }
    public String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }
    public UserPermissionDto getUserPermission(String authHeader) {
        String token = extractToken(authHeader);
        Map<String, Object> payload = decodeToken(token);

        if (payload == null) {
            throw new RuntimeException("Token is invalid or expired");
        }

        String role = (String) payload.get("role_name");
        Integer userId = Integer.parseInt(payload.get("id").toString());

        UserPermissionDto userPermissionDto = new UserPermissionDto();
        userPermissionDto.setUserId(userId);

        if (role == null || (!"ROLE_ADMIN".equals(role) && !"ROLE_STAFF".equals(role))) {
            userPermissionDto.setHasPermission(false);
        } else {
            userPermissionDto.setHasPermission(true);
        }

        return userPermissionDto;
    }




}