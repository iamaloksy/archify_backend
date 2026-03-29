package com.archifyai.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class PuterSignInRequest {

    @NotBlank
    private String puterUuid;

    @NotBlank
    private String username;

    public String getPuterUuid() {
        return puterUuid;
    }

    public void setPuterUuid(String puterUuid) {
        this.puterUuid = puterUuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
