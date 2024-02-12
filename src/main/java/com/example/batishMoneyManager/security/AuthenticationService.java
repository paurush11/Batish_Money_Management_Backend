package com.example.batishMoneyManager.security;

import com.example.batishMoneyManager.User.Role;
import com.example.batishMoneyManager.User.User;
import com.example.batishMoneyManager.jpa.UserResourceRepository;
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
        var userExists = repository.findByUserName(request.getUserName());
        if(userExists.isPresent()){
            System.out.println(userExists.get());
            System.out.println("Phle se tha bc");
            var jwtToken = jwtUtil.generateToken(userExists.get());
            return AuthResponse.builder().Token(jwtToken).ExistedBefore(true).build();
        }
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
        return AuthResponse.builder().Token(jwtToken).ExistedBefore(false).build();
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
        return AuthResponse.builder().Token(jwtToken).ExistedBefore(true).build();
    }
}
