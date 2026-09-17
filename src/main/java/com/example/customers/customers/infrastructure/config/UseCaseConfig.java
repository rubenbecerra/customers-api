package com.example.customers.customers.infrastructure.config;

import com.example.customers.customers.application.usecase.DeleteCustomerUseCase;
import com.example.customers.customers.application.usecase.GetCustomerUseCase;
import com.example.customers.customers.application.usecase.RegisterCustomerUseCase;
import com.example.customers.customers.application.usecase.UpdateCustomerUseCase;
import com.example.customers.customers.domain.repository.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UseCaseConfig {

    @Bean
    public DeleteCustomerUseCase deleteCustomerUseCase(CustomerRepository customerRepository) {
        return new DeleteCustomerUseCase(customerRepository);
    }

    @Bean
    public RegisterCustomerUseCase registerCustomerUseCase(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return new RegisterCustomerUseCase(customerRepository, passwordEncoder);
    }

    @Bean
    public GetCustomerUseCase getCustomerUseCase(CustomerRepository customerRepository) {
        return new GetCustomerUseCase(customerRepository);
    }

    @Bean
    public UpdateCustomerUseCase updateCustomerUseCase(CustomerRepository customerRepository) {
        return new UpdateCustomerUseCase(customerRepository);
    }
}