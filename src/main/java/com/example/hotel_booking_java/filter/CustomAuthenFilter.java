package com.example.hotel_booking_java.filter;


import com.example.hotel_booking_java.utils.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAuthenFilter extends OncePerRequestFilter {


    @Autowired
    private JwtHelper jwtHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authenHeader = request.getHeader("Authorization");
        //   System.out.println("kiem tra" + authenHeader);

        if(authenHeader != null && authenHeader.startsWith("Bearer ")) {
            String token = authenHeader.substring(7);

            String data = jwtHelper.decodeToken(token);

            if (data != null){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken("", "", List.of());


                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }


}
