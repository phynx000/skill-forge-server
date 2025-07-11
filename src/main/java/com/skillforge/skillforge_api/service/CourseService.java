package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.dto.mapper.CourseMapper;
import com.skillforge.skillforge_api.dto.request.CourseCreateRequest;
import com.skillforge.skillforge_api.dto.response.CourseDTO;
import com.skillforge.skillforge_api.entity.Course;
import com.skillforge.skillforge_api.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CategoryService categoryService;
    private final UserService userService;


    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, CategoryService categoryService, UserService userService) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    public List<CourseDTO> handleGetAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(courseMapper::toDto)
                .toList();

    }

    @Transactional
    public CourseDTO handleCreateCourse(CourseCreateRequest request) {
        Course course = courseMapper.toEntity(request);
        course.setCategory(request.getCategoryId() != null ? categoryService.handleGetCategoryById(request.getCategoryId()) : null);
        course.setInstructor(userService.getCurrentUser());
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);

    }

    @Transactional
    public CourseDTO handleUpdateCourse(CourseCreateRequest request, Long courseId){
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

        // Update fields from request
        existingCourse.setTitle(request.getTitle());
        existingCourse.setDescription(request.getDescription());
        existingCourse.setCategory(request.getCategoryId() != null ? categoryService.handleGetCategoryById(request.getCategoryId()) : null);
        existingCourse.setPrice(request.getPrice());
        existingCourse.setThumbnailUrl(request.getThumbnailUrl());
        existingCourse.setPublished(request.isPublished());


        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toDto(updatedCourse);
    }




}
