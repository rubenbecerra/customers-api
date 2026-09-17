package com.example.customers.customers.application.usecase;

import com.example.customers.customers.domain.model.Customer;
import com.example.customers.customers.domain.repository.CustomerRepository;

import java.util.List;
import java.util.NoSuchElementException;


public class GetCustomerUseCase implements GetCustomerPort {

    private final CustomerRepository customerRepository;

    public GetCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Client with email " + email + " not found"));
    }
    @Override
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + id + " not found"));
    }
}