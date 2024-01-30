package com.example.batishMoneyManager.security;

import com.example.batishMoneyManager.User.Role;
import com.example.batishMoneyManager.User.User;
import com.example.batishMoneyManager.User.UserResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserResourceRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var password = request.getPassword();

        var user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtUtil.generateToken(user);
        return AuthResponse.builder().Token(jwtToken).build();
    }

    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        var user = repository.findByUserName(request.getUserName()).orElseThrow();
        var jwtToken = jwtUtil.generateToken(user);
        return AuthResponse.builder().Token(jwtToken).build();
    }
}
