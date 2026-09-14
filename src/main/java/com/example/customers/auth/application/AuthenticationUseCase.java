package com.example.customers.auth.application;

import com.example.customers.auth.infrastructure.rest.AuthenticationRequest;
import com.example.customers.auth.infrastructure.rest.AuthenticationResponse;
import com.example.customers.shared.security.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationUseCase {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AuthenticationUseCase(AuthenticationManager authenticationManager,
                                 JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        assert principal != null;
        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = jwtUtil.issueToken(principal.getUsername(),
                java.util.Map.of("roles", roles));

        String firstRole = roles.isEmpty() ? "" : roles.getFirst();

        return new AuthenticationResponse(token, principal.getUsername(), firstRole);    }
}