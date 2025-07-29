package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.request.CourseCreateRequest;
import com.skillforge.skillforge_api.dto.response.CourseDTO;
import com.skillforge.skillforge_api.dto.response.CourseDetailDTO;
import com.skillforge.skillforge_api.dto.response.ResultPaginationDTO;
import com.skillforge.skillforge_api.service.CourseService;
import com.skillforge.skillforge_api.utils.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/api/v1")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    @ApiMessage(value = "Fetch all courses with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllCourses(
            @RequestParam("current")Optional<String> currentOptional,
            @RequestParam("pageSize")Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        int current = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        ResultPaginationDTO coursesDto = this.courseService.handleGetAllCourses(pageable);
        return ResponseEntity.ok().body(coursesDto);

    }

    @PostMapping("/courses")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        CourseDTO courseDTO = courseService.handleCreateCourse(request);
        return ResponseEntity.status(201).body(courseDTO);
    }

    @GetMapping("/courses/category/{categoryId}")
    @ApiMessage(value = "Fetch courses by category ID with pagination")
    public ResponseEntity<ResultPaginationDTO> getCoursesByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        int current = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        ResultPaginationDTO coursesDto = this.courseService.getCoursesByCategoryId(categoryId, pageable);
        return ResponseEntity.ok().body(coursesDto);
    }

    @GetMapping("/courses/{id}")
    @ApiMessage(value = "Fetch course by ID")
    public ResponseEntity<CourseDetailDTO> getCourseById(@PathVariable Long id) {
        CourseDetailDTO courseDetailDTO = this.courseService.getCourseById(id);
        return ResponseEntity.ok().body(courseDetailDTO);
    }

}
