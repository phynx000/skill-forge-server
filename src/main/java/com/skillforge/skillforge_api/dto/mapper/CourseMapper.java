package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.request.CourseCreateRequest;
import com.skillforge.skillforge_api.dto.response.CourseDTO;
import com.skillforge.skillforge_api.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO toDto(Course course){
        if (course == null) return null;
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setThumbnailUrl(course.getThumbnailUrl());
        dto.setPrice(course.getPrice());
        dto.setSlug(course.getSlug());
        dto.setPublished(course.isPublished());
        dto.setCategoryName(course.getCategory() != null ? course.getCategory().getName() : null);
        dto.setInstructorName(course.getInstructor() != null ? course.getInstructor().getFullName() : null);
//        dto.setNumberOfLessons(
//                course.getLessons() != null ? course.getLessons().size() : 0
//        );
        return dto;
    }

    public Course toEntity(CourseCreateRequest request) {
        if (request == null) return null;
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setThumbnailUrl(request.getThumbnailUrl());
        course.setPrice(request.getPrice());
        course.setPublished(request.isPublished());


        return course;

    }
}
