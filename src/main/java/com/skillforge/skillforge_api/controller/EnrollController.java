package com.skillforge.skillforge_api.controller;

import com.skillforge.skillforge_api.dto.request.EnrollmentRequest;
import com.skillforge.skillforge_api.dto.response.EnrollmenrtsDTO;
import com.skillforge.skillforge_api.service.EnrollmentService;
import com.skillforge.skillforge_api.utils.annotation.ApiMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EnrollController {

    private final EnrollmentService enrollmentService;

    public EnrollController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/enrollments/{courseId}")
    @ApiMessage("Lấy thông tin đăng ký khóa học thành công")
    public ResponseEntity<EnrollmenrtsDTO> getEnrollmentDetails(@PathVariable Long courseId) {
        EnrollmentRequest request = new EnrollmentRequest();
        request.setCourseId(courseId);
        EnrollmenrtsDTO dto = enrollmentService.getEnrollmentDetails(request);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/enrollments")
    @ApiMessage("Đăng ký khóa học thành công")
    public ResponseEntity<EnrollmenrtsDTO> enrollInCourse(@RequestBody EnrollmentRequest request) {
        EnrollmenrtsDTO dto = enrollmentService.enrollCourse(request);
        return ResponseEntity.ok(dto);
    }


}
