package com.archifyai.backend.dto.auth;

public class PuterAuthResponse {

    private final String userId;
    private final String username;
    private final String puterUuid;

    public PuterAuthResponse(String userId, String username, String puterUuid) {
        this.userId = userId;
        this.username = username;
        this.puterUuid = puterUuid;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPuterUuid() {
        return puterUuid;
    }
}
