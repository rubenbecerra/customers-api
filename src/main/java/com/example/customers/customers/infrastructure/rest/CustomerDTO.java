package com.example.customers.customers.infrastructure.rest;

public record CustomerDTO (
    Integer id,
    String name,
    String email,
    Integer age,
    String gender
) implements java.io.Serializable {}
