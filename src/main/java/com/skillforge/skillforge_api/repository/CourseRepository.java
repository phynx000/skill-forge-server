package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> , JpaSpecificationExecutor<Course> {
    boolean existsBySlug(String slug);
    Page<Course> findByCategoryId(Long categoryId, Pageable pageable);
}
