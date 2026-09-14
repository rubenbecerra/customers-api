package com.example.customers.auth.infrastructure.rest;


public record AuthenticationResponse(
        String token,
        String name,
        String role
) {
}
