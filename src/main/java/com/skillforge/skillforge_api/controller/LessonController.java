package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.request.LessonRequest;
import com.skillforge.skillforge_api.dto.response.LessonDTO;
import com.skillforge.skillforge_api.service.LessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LessonController {

    private final LessionService lessionService;
    public LessonController(LessionService lessionService) {
        this.lessionService = lessionService;
    }

    @GetMapping("/lessons/{sectionId}")
    public ResponseEntity<List<LessonDTO>> getLessonsBySectionId(
            @PathVariable Long sectionId) {
        List<LessonDTO> lessons = lessionService.getLessonsBySectionId(sectionId);
        return ResponseEntity.ok(lessons);
    }

    @PostMapping("/lessons")
    public ResponseEntity<LessonDTO> createLesson(@RequestBody LessonRequest lessonRequest) throws Throwable {
        LessonDTO createdLesson = lessionService.createLesson(lessonRequest);
        return ResponseEntity.ok(createdLesson);
    }

    @GetMapping("/play-course/lesson/{lessonId}")
    public ResponseEntity<LessonDTO> getLessonDetails(
            @PathVariable("lessonId") Long lessonId) {
        LessonDTO dto = lessionService.getLessonDetails(lessonId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return null;
    }

}
