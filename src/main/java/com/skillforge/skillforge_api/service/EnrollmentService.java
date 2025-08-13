package com.skillforge.skillforge_api.service;

import com.skillforge.skillforge_api.dto.mapper.EnrollmentMapper;
import com.skillforge.skillforge_api.dto.request.EnrollmentRequest;
import com.skillforge.skillforge_api.dto.response.EnrollmenrtsDTO;
import com.skillforge.skillforge_api.entity.Course;
import com.skillforge.skillforge_api.utils.constant.EnrollmentStatus;
import com.skillforge.skillforge_api.entity.Enrollments;
import com.skillforge.skillforge_api.entity.User;
import com.skillforge.skillforge_api.repository.CourseRepository;
import com.skillforge.skillforge_api.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;
    private UserService userService;
    CourseRepository courseRepository;
    EnrollmentMapper enrollmentMapper;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, UserService userService,
                             CourseRepository courseRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.userService = userService;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    public EnrollmenrtsDTO enrollCourse (EnrollmentRequest request) {
        // Kiểm tra đã đăng ký chưa
        if (enrollmentRepository.existsByUserIdAndCourseId(request.getCourseId(), userService.getCurrentUser().getId())) {
            throw new IllegalArgumentException("You have already enrolled in this course.");
        }

        // Tạo mới enrollment
        Enrollments enrollment = new Enrollments();
        enrollment.setUser(userService.getCurrentUser());
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + request.getCourseId()));
        enrollment.setCourse(course);
        enrollmentRepository.save(enrollment);
        EnrollmenrtsDTO dto = enrollmentMapper.toDto(enrollment);
        return dto;
    }

    public boolean isEnrolled(Long courseId) {
        User currentUser = userService.getCurrentUser();
        return enrollmentRepository.existsByUserIdAndCourseId(courseId, currentUser.getId());
    }

    public EnrollmenrtsDTO getEnrollmentDetails(EnrollmentRequest request) {
        User currentUser = userService.getCurrentUser();
        Enrollments enrollment = enrollmentRepository.findByUserIdAndCourseIdAndStatus(
                currentUser.getId(), request.getCourseId(), EnrollmentStatus.ENROLLED);
        if (enrollment == null) {
            System.out.println("Khóa học: " + request.getCourseId() + " chưa được đăng ký bởi người dùng: " + currentUser.getEmail());
        }
        return enrollmentMapper.toDto(enrollment);
    }

}
