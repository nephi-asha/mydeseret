package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.CategoryRequestDto;
import com.mydeseret.mydeseret.dto.CategoryResponseDto;
import com.mydeseret.mydeseret.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDto dto) {
        if (dto == null) return null;
        
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public CategoryResponseDto toResponseDto(Category category) {
        if (category == null) return null;

        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
}