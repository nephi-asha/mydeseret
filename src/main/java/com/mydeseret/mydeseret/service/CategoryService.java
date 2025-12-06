package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.CategoryRequestDto;
import com.mydeseret.mydeseret.dto.CategoryResponseDto;
import com.mydeseret.mydeseret.mapper.CategoryMapper;
import com.mydeseret.mydeseret.model.Category;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.CategoryRepository;
import com.mydeseret.mydeseret.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private com.mydeseret.mydeseret.repository.ItemRepository itemRepository;

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

    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (itemRepository.existsByCategory_CategoryId(id)) {
            
            category.setActive(false);
            categoryRepository.save(category);
            System.out.println("Category " + id + " was SOFT DELETED due to existing items.");
        } else {
            
            categoryRepository.delete(category);
            System.out.println("Category " + id + " was HARD DELETED (No items found).");
        }
    }
}