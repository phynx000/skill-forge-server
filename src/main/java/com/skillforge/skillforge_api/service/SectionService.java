package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.dto.mapper.SectionMapper;
import com.skillforge.skillforge_api.dto.request.SectionRequest;
import com.skillforge.skillforge_api.dto.response.SectionDTO;
import com.skillforge.skillforge_api.entity.Section;
import com.skillforge.skillforge_api.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

   private final SectionRepository sectionRepository;
   private final SectionMapper sectionMapper;

    public SectionService(SectionRepository sectionRepository, SectionMapper sectionMapper) {
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;
    }

    public List<Section> getSectionsByCourseId(Long courseId) {
        List<Section> sections = (List<Section>) sectionRepository.findByCourseId(courseId);
        if (sections.isEmpty()) {
            throw new RuntimeException("No sections found for course with ID: " + courseId);
        }
        return sections;
    }

    public SectionDTO createSection(SectionRequest request) {
//        SectionDTO sectionDTO = new SectionDTO();
        Section section = sectionMapper.toEntity(request);
        return sectionMapper.toDto(sectionRepository.save(section));
      }

}
