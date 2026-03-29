package com.archifyai.backend.controller;

import com.archifyai.backend.dto.project.CreateProjectRequest;
import com.archifyai.backend.dto.project.ProjectPageResponse;
import com.archifyai.backend.dto.project.ProjectDto;
import com.archifyai.backend.dto.project.UpdateProjectNameRequest;
import com.archifyai.backend.dto.project.UpdateRenderRequest;
import com.archifyai.backend.security.AppUserPrincipal;
import com.archifyai.backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ProjectPageResponse list(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(name = "q", defaultValue = "") String query
    ) {
        return projectService.listForUser(principal.getUserId(), page, size, query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto create(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @Valid @RequestBody CreateProjectRequest request
    ) {
        return projectService.create(principal.getUserId(), request);
    }

    @GetMapping("/{id}")
    public ProjectDto getOne(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable String id
    ) {
        return projectService.getByIdForOwner(principal.getUserId(), id);
    }

    @PatchMapping("/{id}/render")
    public ProjectDto updateRender(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable String id,
            @Valid @RequestBody UpdateRenderRequest request
    ) {
        return projectService.updateRender(principal.getUserId(), id, request);
    }

    @PatchMapping("/{id}/name")
    public ProjectDto updateName(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable String id,
            @Valid @RequestBody UpdateProjectNameRequest request
    ) {
        return projectService.updateName(principal.getUserId(), id, request);
    }

    @PostMapping("/{id}/share")
    public ProjectDto share(@AuthenticationPrincipal AppUserPrincipal principal, @PathVariable String id) {
        return projectService.share(principal.getUserId(), id);
    }

    @PostMapping("/{id}/unshare")
    public ProjectDto unshare(@AuthenticationPrincipal AppUserPrincipal principal, @PathVariable String id) {
        return projectService.unshare(principal.getUserId(), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AppUserPrincipal principal, @PathVariable String id) {
        projectService.delete(principal.getUserId(), id);
    }
}

