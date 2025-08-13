package com.skillforge.skillforge_api.dto.response;


import com.skillforge.skillforge_api.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BioDTO {
    private String fullName;
    private String description;
    private List<Skill> skills;
    private List<EducationDTO> education;

    public BioDTO() {

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class EducationDTO {
        private Long id;
        private String degree;
        private String institution;
        private String major;
        private int year;
    }
}
