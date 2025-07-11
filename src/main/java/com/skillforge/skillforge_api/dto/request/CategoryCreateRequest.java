package com.skillforge.skillforge_api.dto.request;


import jakarta.validation.constraints.NotBlank;

// class này sẽ được sử dụng để nhận dữ liệu từ client khi tạo mới một category , nó quyết định những trường nào sẽ được lưu
public class CategoryCreateRequest {

    @NotBlank
    private String name;
    private String description;
    private Long parentCategoryId; // id của category cha, nếu không có thì sẽ là null

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

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

}

