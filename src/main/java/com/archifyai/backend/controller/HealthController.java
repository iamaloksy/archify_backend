package com.archifyai.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(value = {"/", "/health", "/api/health"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String health() {
        return "server is running";
    }
}
