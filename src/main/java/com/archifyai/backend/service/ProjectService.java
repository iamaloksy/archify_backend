package com.archifyai.backend.service;

import com.archifyai.backend.dto.project.CreateProjectRequest;
import com.archifyai.backend.dto.project.ProjectPageResponse;
import com.archifyai.backend.dto.project.ProjectDto;
import com.archifyai.backend.dto.project.UpdateProjectNameRequest;
import com.archifyai.backend.dto.project.UpdateRenderRequest;
import com.archifyai.backend.model.Project;
import com.archifyai.backend.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Objects;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectDto create(String userId, CreateProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setSourceImage(request.getSourceImage());
        project.setThumbnail(request.getThumbnail());
        project.setOwnerId(userId);
        project.setPublic(Boolean.TRUE.equals(request.getIsPublic()));
        Instant now = Instant.now();
        project.setCreatedAt(now);
        project.setUpdatedAt(now);

        return toDto(projectRepository.save(project), false);
    }

        public ProjectPageResponse listForUser(String userId, int page, int size, String query) {
        int safePage = Math.max(0, page);
        int safeSize = Math.min(Math.max(1, size), 50);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Project> data = (query != null && !query.isBlank())
            ? projectRepository.findByOwnerIdAndNameContainingIgnoreCase(userId, query.trim(), pageable)
            : projectRepository.findByOwnerId(userId, pageable);

        return new ProjectPageResponse(
            data.getContent().stream().map(project -> toDto(project, true)).toList(),
            safePage,
            safeSize,
            data.getTotalElements(),
            data.hasNext()
        );
    }

    public ProjectDto getByIdForOwner(String userId, String projectId) {
        Project project = findById(Objects.requireNonNull(projectId));
        if (!project.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
        return toDto(project, false);
    }

    public ProjectDto getShared(String projectId) {
        Project project = findById(Objects.requireNonNull(projectId));
        if (!project.isPublic()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shared project not found");
        }
        return toDto(project, false);
    }

    public ProjectDto updateRender(String userId, String projectId, UpdateRenderRequest request) {
        Project project = findById(Objects.requireNonNull(projectId));
        ensureOwner(project, userId);

        project.setRenderedImage(request.getRenderedImage());
        project.setRenderedPath(request.getRenderedPath());
        if (request.getThumbnail() != null && !request.getThumbnail().isBlank()) {
            project.setThumbnail(request.getThumbnail());
        }
        project.setUpdatedAt(Instant.now());

        return toDto(projectRepository.save(project), false);
    }

    public ProjectDto updateName(String userId, String projectId, UpdateProjectNameRequest request) {
        Project project = findById(Objects.requireNonNull(projectId));
        ensureOwner(project, userId);

        project.setName(request.getName().trim());
        project.setUpdatedAt(Instant.now());

        return toDto(projectRepository.save(project), false);
    }

    public ProjectDto share(String userId, String projectId) {
        Project project = findById(Objects.requireNonNull(projectId));
        ensureOwner(project, userId);

        project.setPublic(true);
        project.setUpdatedAt(Instant.now());
        return toDto(projectRepository.save(project), false);
    }

    public ProjectDto unshare(String userId, String projectId) {
        Project project = findById(Objects.requireNonNull(projectId));
        ensureOwner(project, userId);

        project.setPublic(false);
        project.setUpdatedAt(Instant.now());
        return toDto(projectRepository.save(project), false);
    }

    public void delete(String userId, String projectId) {
        Project project = findById(Objects.requireNonNull(projectId));
        ensureOwner(project, userId);
        projectRepository.delete(Objects.requireNonNull(project));
    }

    private Project findById(String projectId) {
        return projectRepository.findById(Objects.requireNonNull(projectId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private void ensureOwner(Project project, String userId) {
        if (!project.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
    }

    private ProjectDto toDto(Project project, boolean summaryMode) {
        long timestamp = project.getCreatedAt() != null ? project.getCreatedAt().toEpochMilli() : Instant.now().toEpochMilli();
        String sourceImage = summaryMode ? null : project.getSourceImage();
        String renderedImage = summaryMode ? null : project.getRenderedImage();

        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getThumbnail(),
                sourceImage,
                renderedImage,
                project.getRenderedPath(),
                project.getOwnerId(),
                timestamp,
                project.isPublic()
        );
    }
}

