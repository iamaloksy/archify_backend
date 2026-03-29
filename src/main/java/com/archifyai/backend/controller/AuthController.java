package com.archifyai.backend.controller;

import com.archifyai.backend.dto.auth.AuthResponse;
import com.archifyai.backend.dto.auth.AuthUserDto;
import com.archifyai.backend.dto.auth.PuterAuthResponse;
import com.archifyai.backend.dto.auth.PuterSignInRequest;
import com.archifyai.backend.dto.auth.SignInRequest;
import com.archifyai.backend.dto.auth.SignUpRequest;
import com.archifyai.backend.security.AppUserPrincipal;
import com.archifyai.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/signin")
    public AuthResponse signIn(@Valid @RequestBody SignInRequest request) {
        return authService.signIn(request);
    }

    @PostMapping("/puter-signin")
    public PuterAuthResponse puterSignIn(@Valid @RequestBody PuterSignInRequest request) {
        return authService.puterSignIn(request);
    }

    @GetMapping("/me")
    public AuthUserDto me(@AuthenticationPrincipal AppUserPrincipal principal) {
        return authService.me(Objects.requireNonNull(principal).getUserId());
    }
}

