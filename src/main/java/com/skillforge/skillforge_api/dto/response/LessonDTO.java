package com.skillforge.skillforge_api.dto.response;

import com.skillforge.skillforge_api.dto.request.LessonRequest;
import com.skillforge.skillforge_api.entity.Lesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonDTO {
    private Long id;
    private String title;
    private Long sectionId;
    private int orderIndex;
    private String videoUrl;
    private boolean completed;

}
