package com.skillforge.skillforge_api.dto.response;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private List<CategoryDTO> subCategories = new ArrayList<>();

    public CategoryDTO(Long id, String name, String description, List<CategoryDTO> subCategories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subCategories = subCategories;
    }

    public CategoryDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CategoryDTO> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<CategoryDTO> subCategories) {
        this.subCategories = subCategories;
    }
}
