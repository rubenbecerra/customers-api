package com.example.customers.dto;

import jakarta.validation.constraints.Min;

public record CustomerUpdateRequest(
        String name,

        @Min(value = 18, message = "Customer must be at least 18")
        Integer age,
        String gender
) {}
