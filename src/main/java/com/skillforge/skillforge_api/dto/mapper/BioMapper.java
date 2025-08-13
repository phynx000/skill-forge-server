package com.skillforge.skillforge_api.dto.mapper;


import com.skillforge.skillforge_api.dto.response.BioDTO;
import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class BioMapper {

    public BioDTO toBioDTO(User user) {
        BioDTO bioDTO = new BioDTO();
        bioDTO.setFullName(user.getFullName());
        bioDTO.setDescription(user.getDescription());
        bioDTO.setSkills(user.getSkills());
        bioDTO.setEducation(user.getEducation().stream()
                .map(edu -> new BioDTO.EducationDTO(
                        edu.getId(),
                        edu.getDegree(),
                        edu.getInstitution(),
                        edu.getMajor(),
                        edu.getYear()))
                .toList());

        return bioDTO;
    }

}
