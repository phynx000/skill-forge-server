package com.skillforge.skillforge_api.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lessons")
@Getter
@Setter
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @OneToOne
    @JoinColumn(name = "video_id", nullable = true)
    private Video video;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @JoinColumn(name = "course_id" , nullable = true)
    @ManyToOne(cascade = CascadeType.ALL)
    private Course course;
    private int orderIndex;

    @Column(name = "completed", nullable = true)
    private boolean completed;
}
