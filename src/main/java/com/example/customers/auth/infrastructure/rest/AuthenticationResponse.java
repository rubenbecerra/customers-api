package com.example.customers.auth.infrastructure.rest;


public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        String name,
        String role
) {
}
