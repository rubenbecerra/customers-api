package com.example.customers.controller;

import com.example.customers.dto.CustomerDTO;
import com.example.customers.dto.CustomerRegistrationRequest;
import com.example.customers.dto.CustomerUpdateRequest;
import com.example.customers.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CustomerDTO> getCustomersDTO() {
        return customerService.getAllCustomersDTO();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public CustomerDTO getCustomerDTO(@PathVariable("id") Integer id) {
        return customerService.getCustomerDTOById(id);
    }

    @GetMapping("me")
    public CustomerDTO getAuthenticatedCustomer(Authentication authentication) {
        String email = authentication.getName();
        return customerService.getCustomerDTOByEmail(email);
    }

    @PostMapping
    public void registerCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
    }

    @DeleteMapping("me")
    public void deleteAuthenticatedCustomer(Authentication authentication)
    {
        String email = authentication.getName();
        customerService.deleteCustomerByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public void updateCustomer(
            @PathVariable("id") Integer id,
            @RequestBody CustomerUpdateRequest request) {
        customerService.updateCustomer(id, request);
    }

    @PutMapping("me")
    public void updateAuthenticatedCustomer(Authentication authentication,
                                            @RequestBody CustomerUpdateRequest request) {
        String email = authentication.getName();
        customerService.updateCustomerByEmail(email, request);
    }
}
