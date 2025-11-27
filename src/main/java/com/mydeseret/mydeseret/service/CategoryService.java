package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.CategoryRequestDto;
import com.mydeseret.mydeseret.dto.CategoryResponseDto;
import com.mydeseret.mydeseret.mapper.CategoryMapper;
import com.mydeseret.mydeseret.model.Category;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.CategoryRepository;
import com.mydeseret.mydeseret.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryMapper categoryMapper;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto request) {

        Category category = categoryMapper.toEntity(request);

        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponseDto(saved);
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}