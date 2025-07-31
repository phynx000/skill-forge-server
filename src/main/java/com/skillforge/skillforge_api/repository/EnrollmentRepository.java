package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.ENUM.EnrollmentStatus;
import com.skillforge.skillforge_api.entity.Enrollments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollments, Long> {

    // Kiểm tra xem người dùng đã đăng ký khóa học chưa
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    List<Enrollments> findByUserIdAndStatus(Long userId, EnrollmentStatus status);

    // lấy danh sách học viên của khóa học
    List<Enrollments> findByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    // tìm enrollment cụ thể
    Enrollments findByUserIdAndCourseIdAndStatus(Long userId, Long courseId, EnrollmentStatus status);

    // Đếm số học viên của khóa học
    @Query("SELECT COUNT(e) FROM Enrollments e WHERE e.course.id = :courseId AND e.status = :status")
    long countByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    // Lấy tiến độ học tập
    @Query("SELECT AVG(e.progress) FROM Enrollments e WHERE e.course.id = :courseId AND e.status = 'ACTIVE'")
    double getAverageProgressByCourse(@Param("courseId") Long courseId);


}
