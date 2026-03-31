package com.archifyai.backend.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "projects")
public class Project {
    @Id
    private String id;
    private String name;
    private String thumbnail;
    private String sourceImage;
    private String renderedImage;
    private String renderedPath;
    private String ownerId;
    private long timestamp;
    private boolean isPublic;
}
