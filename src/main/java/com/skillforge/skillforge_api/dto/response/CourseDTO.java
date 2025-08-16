package com.skillforge.skillforge_api.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private String thumbnailUrl;
    private String categoryName;
    private String instructorName;
    private String slug;
    private boolean isPublished;
    private int numberOfSections;
    private int numberOfLessons;

}
