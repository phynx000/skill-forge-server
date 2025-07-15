package com.skillforge.skillforge_api.repository;

import com.skillforge.skillforge_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> , JpaSpecificationExecutor<Category> {
    List<Category> findByParentCategoryIsNull();
}
