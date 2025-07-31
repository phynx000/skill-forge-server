package com.skillforge.skillforge_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionRequest {
    private String title;
    private String description;
    private int orderIndex;
    private Long courseId;




}
