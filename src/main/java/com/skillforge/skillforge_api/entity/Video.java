package com.skillforge.skillforge_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int videoLibraryId; // Bunny Stream library ID
    private String guid; // Unique identifier for the video
    private String title;
    private String playURL;
    private String thumbnailUrl;
    private double length; // Duration in seconds
    private int status; // uploading, processing, ready, error
    private boolean isPublic; // Whether the video is public or private
    private Instant createdAt;
    private String availableResolutions; // JSON string of available resolutions

    public Video() {
        // Default constructor
    }

//    @OneToOne
//    @JoinColumn(name = "lession_id", nullable = false)
//    private Lesson lession;
//
//    @ManyToOne
//    @JoinColumn(name = "course_id", nullable = false)
//    private Course course;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
