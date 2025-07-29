package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseId(Long courseId);
    Optional findById(Long id);
    List<Section> findByCourseIdOrderByOrderIndexAsc(Long courseId);
}
