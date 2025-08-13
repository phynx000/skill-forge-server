package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.response.LessonDTO;
import com.skillforge.skillforge_api.dto.response.SectionDTO;
import com.skillforge.skillforge_api.service.PlayCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PlayCourseController {

    PlayCourseService playCourseService;
    public PlayCourseController(PlayCourseService playCourseService) {
        this.playCourseService = playCourseService;
    }

    @GetMapping("/play-course/{courseId}")
    public ResponseEntity<List<SectionDTO>> getListSection(
            @PathVariable("courseId") Long courseId) {
    List<SectionDTO>  sectionDTOs = this.playCourseService.getListSection(courseId);
        return ResponseEntity.ok(sectionDTOs);
    }




}