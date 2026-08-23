package com.example.customers.dto;

public record CustomerDTO (
    Integer id,
    String name,
    String email,
    Integer age,
    String gender
) implements java.io.Serializable {}
