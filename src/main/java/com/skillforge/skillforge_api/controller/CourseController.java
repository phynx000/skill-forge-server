package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.request.CourseCreateRequest;
import com.skillforge.skillforge_api.dto.response.CourseDTO;
import com.skillforge.skillforge_api.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/api/courses")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> coursesDto = courseService.handleGetAllCourses();
        return ResponseEntity.ok(coursesDto);

    }

    @PostMapping("/api/courses")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        CourseDTO courseDTO = courseService.handleCreateCourse(request);
        return ResponseEntity.status(201).body(courseDTO);
    }

}
