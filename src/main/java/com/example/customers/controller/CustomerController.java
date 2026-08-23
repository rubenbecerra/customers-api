package com.example.customers.controller;

import com.example.customers.dto.CustomerDTO;
import com.example.customers.dto.CustomerRegistrationRequest;
import com.example.customers.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDTO> getCustomersDTO() {
        return customerService.getAllCustomersDTO();
    }
    @GetMapping("{id}")
    public CustomerDTO getCustomerDTO(@PathVariable("id") Integer id) {
        return customerService.getCustomerDTOById(id);
    }

    @PostMapping
    public void registerCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("{id}")
    public void updateCustomer(
            @PathVariable("id") Integer id,
            @RequestBody CustomerRegistrationRequest request) {
        customerService.updateCustomer(id, request);
    }
}
