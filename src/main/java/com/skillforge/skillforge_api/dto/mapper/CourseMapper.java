package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.request.CourseCreateRequest;
import com.skillforge.skillforge_api.dto.response.CourseDTO;
import com.skillforge.skillforge_api.dto.response.CourseDetailDTO;
import com.skillforge.skillforge_api.entity.Course;
import com.skillforge.skillforge_api.service.LessionService;
import com.skillforge.skillforge_api.service.ReviewService;
import com.skillforge.skillforge_api.service.SectionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseMapper {

    private static ReviewService reviewService;
    private static LessionService lessionService;
    private static SectionService sectionService;
    private final SectionMapper sectionMapper;

    public CourseMapper(ReviewService reviewService , LessionService lessionService, SectionService sectionService, SectionMapper sectionMapper) {
        this.reviewService = reviewService;
        this.lessionService = lessionService;
        this.sectionService = sectionService;
        this.sectionMapper = sectionMapper;
    }

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

    public CourseDetailDTO toDetailDto(Course course) {
        if (course == null) return null;
        CourseDetailDTO dto = new CourseDetailDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setThumbnailUrl(course.getThumbnailUrl());
        dto.setOriginalPrice(course.getPrice());
        dto.setRating(reviewService.calculateAverageRating(course.getId()));
        dto.setLessonCount(lessionService.getLessonCountByCourseId(course.getId()));
        dto.setReviewCount(reviewService.getTotalReviews(course.getId()));
        dto.setLevel("Beginner"); //sẽ thay đổi sau
        dto.setThumbnailUrl("https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=400&h=250&fit=crop");
        dto.setDuration("10 hours"); //sẽ thay đổi sau
        dto.setLanguage("English"); //sẽ thay đổi sau
        dto.setInstructor(new CourseDetailDTO.CourseInstructor(102L, "John Doe",
                "https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=100&h=100&fit=crop",
                "Senior Instructor", "5 years of experience"));
        dto.setFeatures(List.of("Feature 1", "Feature 2", "Feature 3")); //sẽ thay đổi sau
        dto.setWhatYouLearn(List.of("Learn A", "Learn B", "Learn C")); //sẽ thay đổi sau
        dto.setSections(sectionService.getSectionsByCourseId(course.getId()).stream()
                .map(section -> sectionMapper.toDto(section))
                .toList());
        return dto;
    }




}
