package com.example.customers.customers.application.usecase;

import com.example.customers.customers.domain.model.Customer;
import com.example.customers.customers.domain.repository.CustomerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GetCustomerUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Cacheable(value = "customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Cacheable(value = "customer_by_email", key = "#email")
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Client with email " + email + " not found"));
    }

    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + id + " not found"));
    }
}