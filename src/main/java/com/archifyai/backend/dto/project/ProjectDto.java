package com.archifyai.backend.dto.project;

public record ProjectDto(
        String id,
        String name,
        String thumbnail,
        String sourceImage,
        String renderedImage,
        String renderedPath,
        String ownerId,
        long timestamp,
        boolean isPublic
) {
}

