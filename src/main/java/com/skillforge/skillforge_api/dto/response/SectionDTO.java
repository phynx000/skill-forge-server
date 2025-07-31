package com.skillforge.skillforge_api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SectionDTO {
    private Long id;
    private String title;
    private String description;
    private int orderIndex;
    private Long courseId;
    private List<LessonDTO> lessons;

}
