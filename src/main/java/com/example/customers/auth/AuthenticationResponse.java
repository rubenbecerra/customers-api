package com.example.customers.auth;

import com.example.customers.dto.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {
}
