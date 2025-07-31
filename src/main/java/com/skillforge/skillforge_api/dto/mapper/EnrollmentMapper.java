package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.request.EnrollmentRequest;
import com.skillforge.skillforge_api.dto.response.EnrollmenrtsDTO;
import com.skillforge.skillforge_api.entity.Enrollments;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {
    public EnrollmenrtsDTO toDto(Enrollments enrollment) {
        if (enrollment == null) {
            return null;
        }
        EnrollmenrtsDTO dto = new EnrollmenrtsDTO();
        dto.setId(enrollment.getId());
        dto.setUserId(enrollment.getUser().getId());
        dto.setCourseName(enrollment.getCourse().getTitle());
        dto.setEnrolledAt(enrollment.getEnrolledAt());
        dto.setCompleted(enrollment.isCompleted());
        dto.setProgress(enrollment.getProgress());
        dto.setStatus(enrollment.getStatus());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setPaid(enrollment.isPaid());
        return dto;
    }



}
