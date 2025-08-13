package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.request.LessonRequest;
import com.skillforge.skillforge_api.dto.response.LessonDTO;
import com.skillforge.skillforge_api.entity.Lesson;
import com.skillforge.skillforge_api.entity.Section;
import com.skillforge.skillforge_api.repository.SectionRepository;
import com.skillforge.skillforge_api.service.BunnyStreamService;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {
    SectionRepository sectionRepository;
   private final BunnyStreamService bunnyStreamService;

    public LessonMapper(SectionRepository sectionRepository, BunnyStreamService bunnyStreamService) {
        this.sectionRepository = sectionRepository;
        this.bunnyStreamService = bunnyStreamService;
    }

    public Lesson toEntity(LessonRequest lessonRequest) throws Throwable {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonRequest.getTitle());
        Section section =(Section) sectionRepository.findById(lessonRequest.getSectionId())
                .orElseThrow(() -> new RuntimeException("Section not found with ID: " + lessonRequest.getSectionId()));
        lesson.setSection(section);
        lesson.setOrderIndex(lessonRequest.getOrderIndex());
        lesson.setCourse(section.getCourse());
        return lesson;
    }

    public LessonDTO toDTO(Lesson lesson) {
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setId(lesson.getId());
        lessonDTO.setTitle(lesson.getTitle());
        lessonDTO.setOrderIndex(lesson.getOrderIndex());
        lessonDTO.setSectionId(lesson.getSection().getId());
        return lessonDTO;
    }

    public LessonDTO toPlayerDTO(Lesson lesson) {
        LessonDTO lessonDTO = toDTO(lesson);
        String newHlsLink =  bunnyStreamService.getVideoLink(lesson.getVideo().getGuid());
        System.out.println("HLS Link: " + newHlsLink);
        lessonDTO.setVideoUrl(newHlsLink);
        return lessonDTO;
    }





}
