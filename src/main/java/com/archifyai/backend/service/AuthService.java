package com.archifyai.backend.service;

import com.archifyai.backend.dto.auth.AuthResponse;
import com.archifyai.backend.dto.auth.AuthUserDto;
import com.archifyai.backend.dto.auth.PuterAuthResponse;
import com.archifyai.backend.dto.auth.PuterSignInRequest;
import com.archifyai.backend.dto.auth.SignInRequest;
import com.archifyai.backend.dto.auth.SignUpRequest;
import com.archifyai.backend.model.User;
import com.archifyai.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse signUp(SignUpRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getId(), saved.getEmail());

        return new AuthResponse(token, toDto(saved));
    }

    public AuthResponse signIn(SignInRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, toDto(user));
    }

    public AuthUserDto me(String userId) {
        User user = userRepository.findById(Objects.requireNonNull(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        return toDto(user);
    }

    public PuterAuthResponse puterSignIn(PuterSignInRequest request) {
        String puterUuid = request.getPuterUuid().trim();
        String username = request.getUsername().trim();

        User user = userRepository.findByPuterUuid(puterUuid).orElseGet(() -> {
            User created = new User();
            created.setPuterUuid(puterUuid);
            created.setUsername(username);
            return userRepository.save(created);
        });

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            user.setUsername(username);
            user = userRepository.save(user);
        }

        return new PuterAuthResponse(user.getId(), user.getUsername(), user.getPuterUuid());
    }

    private AuthUserDto toDto(User user) {
        return new AuthUserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}

