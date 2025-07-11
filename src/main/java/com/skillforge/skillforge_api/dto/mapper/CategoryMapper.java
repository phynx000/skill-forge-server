package com.skillforge.skillforge_api.dto.mapper;

import com.skillforge.skillforge_api.dto.response.CategoryDTO;
import com.skillforge.skillforge_api.entity.Category;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(Category category) {
        if (category == null) return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());

        if (category.getSubCategories() != null) {
            dto.setSubCategories(
                    category.getSubCategories().stream()
                            .map(this::toDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
