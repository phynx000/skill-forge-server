package com.skillforge.skillforge_api.controller;


import com.skillforge.skillforge_api.dto.request.CategoryCreateRequest;
import com.skillforge.skillforge_api.dto.response.CategoryDTO;
import com.skillforge.skillforge_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/api/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        CategoryDTO categoryDTO = categoryService.handleCreateCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }

    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryDTO>> getCategoryTree() {
        List<CategoryDTO> result = categoryService.handleGetTreeCategy();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.handleDeleteCategory(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(205)).body("Category deleted successfully");
    }



}
