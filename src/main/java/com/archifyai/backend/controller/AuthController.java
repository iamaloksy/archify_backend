package com.archifyai.backend.controller;

import com.archifyai.backend.model.User;
import com.archifyai.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import java.util.Optional;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserRepository userRepository;
    
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        if (request == null || request.getPuterUuid() == null || request.getPuterUuid().isBlank()) {
            return ResponseEntity.badRequest().body(AuthResponse.error("puterUuid is required"));
        }

        String puterUuid = request.getPuterUuid().trim();
        String normalizedUsername = (request.getUsername() == null || request.getUsername().isBlank())
                ? "Puter User"
                : request.getUsername().trim();

        Optional<User> existing = userRepository.findById(puterUuid);
        User user;
        if (existing.isPresent()) {
            user = existing.get();
            // Update username if it changed on Puter
            if (!Objects.equals(user.getUsername(), normalizedUsername)) {
                user.setUsername(normalizedUsername);
                user = userRepository.save(user);
            }
        } else {
            user = new User();
            user.setId(puterUuid);
            user.setUsername(normalizedUsername);
            user = userRepository.save(user);
        }
        
        AuthResponse response = new AuthResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setToken("dummy-dev-jwt");
        return ResponseEntity.ok(response);
    }
    
    @Data
    public static class AuthRequest {
        private String puterUuid;
        private String username;
    }
    
    @Data
    public static class AuthResponse {
        private String id;
        private String username;
        private String token;
        private String message;

        public static AuthResponse error(String message) {
            AuthResponse response = new AuthResponse();
            response.setMessage(message);
            return response;
        }
    }
}
