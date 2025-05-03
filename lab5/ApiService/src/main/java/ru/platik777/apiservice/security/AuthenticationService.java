package ru.platik777.apiservice.security;

import lombok.RequiredArgsConstructor;
import models.OwnerDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.platik777.entities.Role;
import ru.platik777.ownerservice.services.OwnerService;
import securityModels.AuthenticationRequest;
import securityModels.AuthenticationResponse;
import securityModels.RegisterRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final OwnerService ownerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = OwnerDetails.builder()
                .name(request.getName())
                .birthday(request.getBirthday())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        System.out.println(user);
        ownerService.saveOwner(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = ownerService.getOwnerByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

