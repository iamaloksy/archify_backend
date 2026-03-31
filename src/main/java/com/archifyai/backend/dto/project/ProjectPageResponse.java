package com.archifyai.backend.dto.project;

import java.util.List;

public record ProjectPageResponse(
        List<ProjectDto> items,
        int page,
        int size,
        long total,
        boolean hasNext
) {
}

