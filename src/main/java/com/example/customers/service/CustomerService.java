package com.example.customers.service;

import com.example.customers.dto.CustomerDTO;
import com.example.customers.dto.CustomerRegistrationRequest;
import com.example.customers.entity.Customer;
import com.example.customers.mapper.CustomerDTOMapper;
import com.example.customers.repository.CustomerRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "customers")
    public List<CustomerDTO> getAllCustomersDTO() {
        return customerRepository.findAll().stream().map(customerDTOMapper).toList();
    }
    @Cacheable(value = "customer", key = "#id")
    public CustomerDTO getCustomerDTOById(Integer id) {
        return customerRepository.findById(id).map(customerDTOMapper)
                .orElseThrow(() -> new NoSuchElementException("Client with ID " + id + " not found"));
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + id + " not found"));
    }
    @Caching(evict = {
            @CacheEvict(value = "customers", allEntries = true),
            @CacheEvict(value = "customer", allEntries = true)
    })
    public void addCustomer(CustomerRegistrationRequest request) {

        if (customerRepository.existsCustomerByEmail(request.email())) {
            throw new IllegalArgumentException("email already taken");
        }
        Customer customer = new Customer(
                request.name(),
                request.email(),
                request.age(),
                request.gender(),
                passwordEncoder.encode(request.password())
        );

        customerRepository.save(customer);
    }
    @Caching(evict = {
            @CacheEvict(value = "customers", allEntries = true),
            @CacheEvict(value = "customer", allEntries = true)
    })
    public void deleteCustomer(Integer id) {
        boolean exists = customerRepository.existsById(id);
        if (!exists) {
            throw new NoSuchElementException("Client with ID " + id + " doesn't exist");
        }
        customerRepository.deleteById(id);
    }
    @Caching(evict = {
            @CacheEvict(value = "customers", allEntries = true),
            @CacheEvict(value = "customer", allEntries = true)
    })
    public void updateCustomer(Integer id, CustomerRegistrationRequest request) {
        Customer customer = getCustomerById(id);
        boolean changes = false;
        if (request.name() != null && !request.name().isEmpty() && !request.name().equals(customer.getName())) {
            customer.setName(request.name());
            changes = true;
        }
        if (request.email() != null && !request.email().isEmpty() && !request.email().equals(customer.getEmail())) {
            if (customerRepository.existsCustomerByEmail(request.email())) {
                throw new DataIntegrityViolationException("email already taken");
            }
            customer.setEmail(request.email());
            changes = true;
        }
        if (request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            changes = true;
        }
        if (request.gender() != null && !request.gender().isEmpty() && !request.gender().equals(customer.getGender())) {
            customer.setGender(request.gender());
            changes = true;
        }
        if (!changes) {
            throw new IllegalArgumentException("No data changes found");
        }

        customerRepository.save(customer);
    }

}
