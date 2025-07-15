package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.dto.mapper.CourseMapper;
import com.skillforge.skillforge_api.dto.request.CourseCreateRequest;
import com.skillforge.skillforge_api.dto.response.CourseDTO;
import com.skillforge.skillforge_api.dto.response.Meta;
import com.skillforge.skillforge_api.dto.response.ResultPaginationDTO;
import com.skillforge.skillforge_api.entity.Course;
import com.skillforge.skillforge_api.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ResultPaginationDTO handleGetAllCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber()+ 1); // Convert to 1-based index for the response
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(courses.getTotalPages());
        meta.setTotalItems(courses.getTotalElements());
        result.setMeta(meta);
        result.setResults(courses.getContent().stream()
                .map(courseMapper::toDto)
                .toList());
        return result;

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
