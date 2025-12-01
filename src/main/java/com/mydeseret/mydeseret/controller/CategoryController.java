package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.CategoryRequestDto;
import com.mydeseret.mydeseret.dto.CategoryResponseDto;
import com.mydeseret.mydeseret.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    // GETs All Categories
    @GetMapping
    @PreAuthorize("hasAuthority('CATEGORY_READ')")
    public ResponseEntity<List<CategoryResponseDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}