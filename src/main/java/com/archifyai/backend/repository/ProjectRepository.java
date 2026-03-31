package com.archifyai.backend.repository;
import com.archifyai.backend.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepository extends MongoRepository<Project, String> {
    Page<Project> findByOwnerId(String ownerId, Pageable pageable);
    Page<Project> findByOwnerIdAndNameContainingIgnoreCase(String ownerId, String name, Pageable pageable);
}
