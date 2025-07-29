package com.skillforge.skillforge_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseDetailDTO {
    private Long id;
    private String title;
    private String shortDescription;
    private String description;
    private CourseInstructor instructor;
    private String lastUpdated;
    private double originalPrice;
    private double discountPrice;
    private double rating;
    private int lessonCount;
    private int reviewCount;
    private String level;
    private String thumbnailUrl;
    private String duration;
    private String language;
    private List<String> features;
    private List<String> whatYouLearn;
    private List<SectionDTO> sections;





    @Setter
    @Getter
    class LessonMetadata {
        private Long id;
        private String title;
        private int orderIndex;
        private String duration;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CourseInstructor {
        private Long id;
        private String name;
        private String avatar;
        private String title;
        private String experience;
    }
}
