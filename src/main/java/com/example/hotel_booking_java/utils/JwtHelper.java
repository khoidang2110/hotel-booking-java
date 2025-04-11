package com.example.hotel_booking_java.utils;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String data) {


        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.builder().subject(data)
                .signWith(key)
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .compact();
    }

    public String decodeToken(String token) {





        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        String data = null;
        try{
            data = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();

        }
        catch(ExpiredJwtException e){

            System.out.println("Token expired");
        }catch(JwtException e){
            System.out.println("decode error");
        }

        return data;
    }

}
