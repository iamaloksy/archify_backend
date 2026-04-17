package com.archifyai.backend.repository;
import com.archifyai.backend.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

public interface ProjectRepository extends MongoRepository<Project, String> {
    @Query(value = "{ '$and': [ { '$or': [ { 'ownerId': ?0 }, { 'isPublic': true } ] }, { 'name': { $regex: ?1, $options: 'i' } } ] }")
    Page<Project> findVisibleProjects(String ownerId, String name, Pageable pageable);
}
