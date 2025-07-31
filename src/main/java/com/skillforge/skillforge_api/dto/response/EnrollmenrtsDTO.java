package com.skillforge.skillforge_api.dto.response;


import com.skillforge.skillforge_api.entity.ENUM.EnrollmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class EnrollmenrtsDTO {
    private Long id;
    private Long userId;
    private Long courseId;
    private String courseName;
    private String courseDescription;
    private Instant enrolledAt;
    private boolean isCompleted;
    private double progress;
    private EnrollmentStatus status;
    private boolean isPaid;

}
