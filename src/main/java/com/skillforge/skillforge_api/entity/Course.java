package com.skillforge.skillforge_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skillforge.skillforge_api.utils.SecurityUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @Column(nullable = false) // title should not be null
    private double price;

    @Column(unique = false, length = 100) // slug should be unique and not null
    private String slug; // unique identifier for the course, often used in URLs

    @Column(columnDefinition = "TEXT")
    private String description;
    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;
    private boolean isPublished;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GTM+7")
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;

    public Course(Long courseId) {
        this.id = courseId;
    }

    public Course() {

    }


    public String generateSlug() {
        // Simple slug generation logic, can be improved
        return title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtils.getCurrentUserLogin().isPresent() == true ? SecurityUtils.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
        this.slug = generateSlug();
        if (this.title != null) {
            this.slug = generateSlug();
        }
    }

}