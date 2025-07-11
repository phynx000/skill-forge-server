package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsBySlug(String slug);
}
