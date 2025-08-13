package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Course;
import com.skillforge.skillforge_api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> , JpaSpecificationExecutor<Course> {
    boolean existsBySlug(String slug);
    Page<Course> findByCategoryId(Long categoryId, Pageable pageable);
    Optional<Course> findById(Long id);
    @Query("SELECT c.instructor FROM Course c WHERE c.id = :courseId")
    Optional<User> findInstructorByCourseId(@Param("courseId") Long courseId);

}
