package com.example.customers.auth;

public record AuthenticationRequest(
        String username,
        String password
) {

}
