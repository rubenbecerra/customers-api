package com.example.customers.customers.application.usecase;

import com.example.customers.customers.domain.model.Customer;
import com.example.customers.customers.domain.repository.CustomerRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeleteCustomerUseCase {

    private final CustomerRepository customerRepository;

    public DeleteCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Caching(evict = {
            @CacheEvict(value = "customers", allEntries = true),
            @CacheEvict(value = "customer_by_email", allEntries = true)
    })
    public void deleteById(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new NoSuchElementException("Client with ID " + id + " doesn't exist");
        }
        customerRepository.deleteById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "customers", allEntries = true),
            @CacheEvict(value = "customer_by_email", allEntries = true)
    })
    public void deleteByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Client with email " + email + " doesn't exist"));
        customerRepository.delete(customer);
    }
}