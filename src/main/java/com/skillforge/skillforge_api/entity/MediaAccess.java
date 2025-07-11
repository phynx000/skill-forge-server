package com.skillforge.skillforge_api.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class MediaAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String mediaUrl;
    private String mediaType; // e.g., "video", "audio", "image"
    private Instant uploadAt;


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Instant getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(Instant uploadAt) {
        this.uploadAt = uploadAt;
    }
}
