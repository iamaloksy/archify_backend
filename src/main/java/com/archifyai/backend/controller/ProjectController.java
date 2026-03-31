package com.archifyai.backend.controller;

import com.archifyai.backend.model.Project;
import com.archifyai.backend.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestHeader(value = "x-user-id", required = false) String ownerId, @RequestBody CreateProjectRequest req) {
        Project p = new Project();
        p.setName(req.getName());
        p.setThumbnail(req.getThumbnail());
        p.setSourceImage(req.getSourceImage());
        p.setOwnerId(ownerId != null ? ownerId : "anonymous");
        p.setTimestamp(System.currentTimeMillis());
        p.setPublic(req.isPublic());
        return ResponseEntity.ok(projectRepository.save(p));
    }

    @GetMapping
    public ResponseEntity<ProjectPageResponse> getProjects(
            @RequestHeader(value = "x-user-id", required = false) String ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "") String q) {
            
        String user = ownerId != null ? ownerId : "anonymous";
        Page<Project> projects;
        PageRequest pageReq = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        if (q != null && !q.trim().isEmpty()) {
            projects = projectRepository.findByOwnerIdAndNameContainingIgnoreCase(user, q, pageReq);
        } else {
            projects = projectRepository.findByOwnerId(user, pageReq);
        }
        
        ProjectPageResponse resp = new ProjectPageResponse();
        resp.setItems(projects.getContent());
        resp.setPage(page);
        resp.setSize(size);
        resp.setTotal((int) projects.getTotalElements());
        resp.setHasNext(projects.hasNext());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable String id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/render")
    public ResponseEntity<Project> updateRender(@PathVariable String id, @RequestBody UpdateRenderRequest req) {
        return projectRepository.findById(id).map(p -> {
            p.setRenderedImage(req.getRenderedImage());
            p.setRenderedPath(req.getRenderedPath());
            if (req.getThumbnail() != null) p.setThumbnail(req.getThumbnail());
            return ResponseEntity.ok(projectRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Project> updateName(@PathVariable String id, @RequestBody UpdateNameRequest req) {
        return projectRepository.findById(id).map(p -> {
            p.setName(req.getName());
            return ResponseEntity.ok(projectRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Project> share(@PathVariable String id) {
        return projectRepository.findById(id).map(p -> {
            p.setPublic(true);
            return ResponseEntity.ok(projectRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/unshare")
    public ResponseEntity<Project> unshare(@PathVariable String id) {
        return projectRepository.findById(id).map(p -> {
            p.setPublic(false);
            return ResponseEntity.ok(projectRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class CreateProjectRequest {
        private String name;
        private String thumbnail;
        private String sourceImage;
        private boolean isPublic;
    }
    
    @Data
    public static class UpdateRenderRequest {
        private String renderedImage;
        private String renderedPath;
        private String thumbnail;
    }
    
    @Data
    public static class UpdateNameRequest {
        private String name;
    }
    
    @Data
    public static class ProjectPageResponse {
        private List<Project> items;
        private int page;
        private int size;
        private int total;
        private boolean hasNext;
    }
}
