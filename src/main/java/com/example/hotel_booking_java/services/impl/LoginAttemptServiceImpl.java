package com.example.hotel_booking_java.services.impl;


import com.example.hotel_booking_java.services.LoginAttemptService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final Cache<String, Integer> attemptsCache;

    private static final int MAX_ATTEMPTS = 3;

    public LoginAttemptServiceImpl() {
        this.attemptsCache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .maximumSize(500)
                .build();
    }

    @Override
    public void loginFailed(String email) {
        Integer attempts = attemptsCache.getIfPresent(email);
        if (attempts == null) {
            attemptsCache.put(email, 1);
        } else {
            attemptsCache.put(email, attempts + 1);
        }
    }

    @Override
    public boolean isBlocked(String email) {
        Integer attempts = attemptsCache.getIfPresent(email);
        return attempts != null && attempts >= MAX_ATTEMPTS;
    }

    @Override
    public void loginSucceeded(String email) {
        attemptsCache.invalidate(email);
    }
}
