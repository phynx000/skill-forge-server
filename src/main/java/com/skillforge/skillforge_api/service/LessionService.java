package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.dto.mapper.LessonMapper;
import com.skillforge.skillforge_api.dto.request.LessonRequest;
import com.skillforge.skillforge_api.dto.response.LessonDTO;
import com.skillforge.skillforge_api.entity.Lesson;
import com.skillforge.skillforge_api.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessionService {

    private final LessonMapper lessonMapper;
    private final BunnyStreamService bunnyStreamService;
    LessonRepository lessonRepository;

    public LessionService(LessonRepository lessonRepository, LessonMapper lessonMapper, BunnyStreamService bunnyStreamService) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.bunnyStreamService = bunnyStreamService;
    }

    public List<LessonDTO> getLessonsBySectionId(Long sectionId) {
        List<Lesson> lessons = (List<Lesson>) lessonRepository.findBySectionIdOrderByOrderIndexAsc(sectionId);
        if (lessons.isEmpty()) {
            throw new RuntimeException("No lessons found for section with ID: " + sectionId);
        }

        List<LessonDTO> lessonDTOs = lessons.stream().map(lessonMapper::toDTO).toList();
        return lessonDTOs;
    }

    public LessonDTO createLesson(LessonRequest request) throws Throwable {
        Lesson lesson = lessonMapper.toEntity(request);
        return lessonMapper.toDTO(lessonRepository.save(lesson));

    }

    public int getLessonCountByCourseId(Long courseId) {
        List<Lesson> lessons = (List<Lesson>) lessonRepository.findByCourseId(courseId);
        return lessons.size();
    }

    public LessonDTO getLessonDetails(Long lessonId) {
        Optional<Lesson> lesson = lessonRepository.findById(lessonId);
        if (lesson.isEmpty()) {
            throw new RuntimeException("Lesson not found with ID: " + lessonId);
        }
        LessonDTO dto = lessonMapper.toDTO(lesson.get());
        dto.setVideoUrl(bunnyStreamService.getVideoLink(lesson.get().getVideo().getGuid()));
        return dto;
    }

}
