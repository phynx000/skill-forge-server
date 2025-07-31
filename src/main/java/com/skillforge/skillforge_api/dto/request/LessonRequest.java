package com.skillforge.skillforge_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonRequest {
    private String title;
    private Long sectionId;
    private int orderIndex;

    // You can add validation annotations if needed, e.g., @NotBlank for title

}
