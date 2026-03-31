package com.archifyai.backend.dto.auth;

public record AuthResponse(String token, AuthUserDto user) {
}

