package com.example.customers.auth.infrastructure.rest;

public record AuthenticationRequest(
        String username,
        String password
) {

}
