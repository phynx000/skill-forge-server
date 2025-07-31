package com.skillforge.skillforge_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollmentRequest {
    @NotNull(message = "Course ID is required")
    private Long courseId;


}
