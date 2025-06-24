package com.skillforge.skillforge_api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MediaAccess {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String mediaUrl;
    private String mediaType; // e.g., "video", "audio", "image"
    private LocalDateTime uploadAt;


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

    public LocalDateTime getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(LocalDateTime uploadAt) {
        this.uploadAt = uploadAt;
    }
}
