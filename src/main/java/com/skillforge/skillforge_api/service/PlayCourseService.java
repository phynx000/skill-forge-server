package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.dto.mapper.LessonMapper;
import com.skillforge.skillforge_api.dto.mapper.SectionMapper;
import com.skillforge.skillforge_api.dto.response.LessonDTO;
import com.skillforge.skillforge_api.dto.response.SectionDTO;
import com.skillforge.skillforge_api.entity.Lesson;
import com.skillforge.skillforge_api.entity.Section;
import com.skillforge.skillforge_api.repository.LessonRepository;
import com.skillforge.skillforge_api.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayCourseService {

    private SectionMapper sectionMapper;
    private SectionRepository sectionRepository;
    private LessonRepository lessonRepository;
    private LessonMapper lessonMapper;

    public PlayCourseService(SectionMapper sectionMapper, SectionRepository sectionRepository,
                             LessonRepository lessonRepository, LessonMapper lessonMapper) {
        this.sectionMapper = sectionMapper;
        this.sectionRepository = sectionRepository;
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }



    public List<SectionDTO> getListSection(Long courseId) {
        List<Section> sections = sectionRepository.findByCourseIdOrderByOrderIndexAsc(courseId);

        List<SectionDTO> sectionDTOs = sections.stream()
                .map(section -> sectionMapper.toDto(section))// toDto: Section -> SectionDTO
                .toList();

        // Gán danh sách bài học cho từng SectionDTO
        sectionDTOs.forEach(sectionDTO -> {
            List<LessonDTO> lessonDTOs = toLessonDTOList(sectionDTO.getId());
            sectionDTO.setLessons(lessonDTOs);
        });

        return sectionDTOs;
    }

    public List<LessonDTO> toLessonDTOList (Long sectionId) {
        List<Lesson> lessons = (List<Lesson>) lessonRepository.findBySectionIdOrderByOrderIndexAsc(sectionId);
        List<LessonDTO> lessonDTOs = lessons.stream().map(
                lesson -> lessonMapper.toPlayerDTO(lesson) // toDto: Lesson -> LessonDTO
        ).toList();
        return lessonDTOs;

    }
}
