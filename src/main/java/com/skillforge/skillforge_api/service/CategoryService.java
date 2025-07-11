package com.skillforge.skillforge_api.service;


import com.skillforge.skillforge_api.dto.request.CategoryCreateRequest;
import com.skillforge.skillforge_api.dto.response.CategoryDTO;
import com.skillforge.skillforge_api.entity.Category;
import com.skillforge.skillforge_api.dto.mapper.CategoryMapper;
import com.skillforge.skillforge_api.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private  CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @Transactional
    public CategoryDTO handleCreateCategory(CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        // Nếu có parentId thì lấy parentCategory
        if (request.getParentCategoryId() != null) {
            Category parent = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            category.setParentCategory(parent);
        }

        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved); // dùng DTO để trả về
    }

    public List<CategoryDTO> handleGetTreeCategy() {
        List<Category> roots = categoryRepository.findByParentCategoryIsNull();
        List<CategoryDTO> result = roots.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
        return result;
    }

    public void handleDeleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        // Xóa tất cả subCategories trước
        for (Category subCategory : category.getSubCategories()) {
            handleDeleteCategory(subCategory.getId());
        }
        // Xóa category hiện tại
        categoryRepository.delete(category);
    }

    public Category handleGetCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return category;
    }



}
