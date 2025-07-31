package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.mapper.SectionMapper;
import com.skillforge.skillforge_api.dto.request.SectionRequest;
import com.skillforge.skillforge_api.dto.response.SectionDTO;
import com.skillforge.skillforge_api.entity.Lesson;
import com.skillforge.skillforge_api.entity.Section;
import com.skillforge.skillforge_api.service.LessionService;
import com.skillforge.skillforge_api.service.SectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SectionController {

    private final SectionService sectionService;
    private final SectionMapper sectionMapper;

    public SectionController(SectionService sectionService, SectionMapper sectionMapper) {
        this.sectionService = sectionService;
        this.sectionMapper = sectionMapper;
    }

    @GetMapping("/sections/{courseId}")
    public ResponseEntity<List<SectionDTO>> getSectionsByCourse(
            @PathVariable("courseId") Long courseId
    ) {
        List<Section> sections = sectionService.getSectionsByCourseId(courseId);
        if (sections.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<SectionDTO> sectionDTOs = sections.stream()
                .map(sectionMapper::toDto)
                .toList();
        return ResponseEntity.ok(sectionDTOs);
    }

    @PostMapping("/sections")
    public ResponseEntity<SectionDTO> createSection(
            @RequestBody SectionRequest sectionRequest
            ) {
        SectionDTO sectionDTO = sectionService.createSection(sectionRequest);
        return ResponseEntity.ok(sectionDTO);
    }
}
