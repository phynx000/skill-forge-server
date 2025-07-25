package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.request.SectionRequest;
import com.skillforge.skillforge_api.dto.response.SectionDTO;
import com.skillforge.skillforge_api.entity.Course;
import com.skillforge.skillforge_api.entity.Section;
import com.skillforge.skillforge_api.repository.CourseRepository;
import org.springframework.stereotype.Component;


@Component
public class SectionMapper {
    CourseRepository courseRepository;

    public SectionMapper(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Section toEntity(SectionRequest request) {
        Section section = new Section();
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + request.getCourseId()));
        section.setTitle(request.getTitle());
        section.setDescription(request.getDescription());
        section.setOrderIndex(request.getOrderIndex());
        section.setCourse(course);
        return section;

    }

    public SectionDTO toDto(Section section) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(section.getId());
        sectionDTO.setTitle(section.getTitle());
        sectionDTO.setDescription(section.getDescription());
        sectionDTO.setOrderIndex(section.getOrderIndex());
        sectionDTO.setCourseId(section.getCourse().getId());
        return sectionDTO;
    }

}
