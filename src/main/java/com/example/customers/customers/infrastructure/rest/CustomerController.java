package com.example.customers.customers.infrastructure.rest;

import com.example.customers.customers.application.usecase.DeleteCustomerUseCase;
import com.example.customers.customers.application.usecase.GetCustomerUseCase;
import com.example.customers.customers.application.usecase.RegisterCustomerUseCase;
import com.example.customers.customers.application.usecase.UpdateCustomerUseCase;
import com.example.customers.customers.domain.model.Customer;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerController(
            RegisterCustomerUseCase registerCustomerUseCase,
            GetCustomerUseCase getCustomerUseCase,
            UpdateCustomerUseCase updateCustomerUseCase,
            DeleteCustomerUseCase deleteCustomerUseCase,
            CustomerDTOMapper customerDTOMapper
    ) {
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
        this.deleteCustomerUseCase = deleteCustomerUseCase;
        this.customerDTOMapper = customerDTOMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CustomerDTO> getCustomersDTO() {
        return getCustomerUseCase.getAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public CustomerDTO getCustomerDTO(@PathVariable("id") Integer id) {
        Customer customer = getCustomerUseCase.getCustomerById(id);
        return customerDTOMapper.apply(customer);
    }

    @GetMapping("me")
    public CustomerDTO getAuthenticatedCustomer(Authentication authentication) {
        String email = authentication.getName();
        Customer customer = getCustomerUseCase.getCustomerByEmail(email);
        return customerDTOMapper.apply(customer);
    }

    @PostMapping
    public void registerCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
        registerCustomerUseCase.execute(
                request.name(),
                request.email(),
                request.age(),
                request.gender(),
                request.password()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        deleteCustomerUseCase.deleteById(id);
    }

    @DeleteMapping("me")
    public void deleteAuthenticatedCustomer(Authentication authentication) {
        String email = authentication.getName();
        deleteCustomerUseCase.deleteByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public void updateCustomer(
            @PathVariable("id") Integer id,
            @RequestBody CustomerUpdateRequest request) {
        updateCustomerUseCase.update(id, request.name(), request.age(), request.gender());
    }

    @PutMapping("me")
    public void updateAuthenticatedCustomer(Authentication authentication,
                                            @RequestBody CustomerUpdateRequest request) {
        String email = authentication.getName();
        updateCustomerUseCase.updateByEmail(email, request.name(), request.age(), request.gender());
    }
}