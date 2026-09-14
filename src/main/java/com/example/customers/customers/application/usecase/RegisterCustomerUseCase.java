package com.example.customers.customers.application.usecase;

import com.example.customers.customers.domain.model.Customer;
import com.example.customers.customers.domain.repository.CustomerRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterCustomerUseCase(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Caching(evict = {
            @CacheEvict(value = "customers", allEntries = true),
            @CacheEvict(value = "customer_by_email", allEntries = true)
    })
    public void execute(String name, String email, Integer age, String gender, String rawPassword) {
        if (customerRepository.existsCustomerByEmail(email)) {
            throw new IllegalArgumentException("email already taken");
        }

        Customer customer = new Customer(
                name,
                email,
                age,
                gender,
                passwordEncoder.encode(rawPassword)
        );

        customerRepository.save(customer);
    }
}