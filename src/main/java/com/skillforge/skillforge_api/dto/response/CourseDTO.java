package com.skillforge.skillforge_api.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {
    private Long id;
    private String title;
    private double price;
    private String description;
    private String thumbnailUrl;
    private String categoryName;
    private String instructorName;
    private String slug;
    private boolean isPublished;
    private int numberOfSections;
    private int numberOfLessons;

}
