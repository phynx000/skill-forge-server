package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Collection<Lesson> findBySectionIdOrderByOrderIndexAsc(Long sectionId);
    Collection<Lesson> findByCourseId(Long courseId);


}
