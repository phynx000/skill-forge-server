package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.dto.mapper.LessonMapper;
import com.skillforge.skillforge_api.dto.request.LessonRequest;
import com.skillforge.skillforge_api.dto.response.LessonDTO;
import com.skillforge.skillforge_api.entity.Lesson;
import com.skillforge.skillforge_api.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessionService {

    private final LessonMapper lessonMapper;
    LessonRepository lessonRepository;

    public LessionService(LessonRepository lessonRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    public List<LessonDTO> getLessonsBySectionId(Long sectionId) {
        List<Lesson> lessons = (List<Lesson>) lessonRepository.findBySectionId(sectionId);
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

}
