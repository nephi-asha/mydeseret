package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.ItemRequestDto;
import com.mydeseret.mydeseret.dto.ItemResponseDto;
import com.mydeseret.mydeseret.mapper.ItemMapper;
import com.mydeseret.mydeseret.model.Category;
import com.mydeseret.mydeseret.model.Item;
import com.mydeseret.mydeseret.model.User;
import com.mydeseret.mydeseret.repository.CategoryRepository;
import com.mydeseret.mydeseret.repository.ItemRepository;
import com.mydeseret.mydeseret.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    @Autowired private ItemRepository itemRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ItemMapper itemMapper;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public ItemResponseDto createItem(ItemRequestDto request) {
               
        Item item = itemMapper.toEntity(request);

        if (request.getCategoryId() != null) {
                Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                
                item.setCategory(category);
            }

        if (itemRepository.findBySku(request.getSku()).isPresent()) {
            throw new RuntimeException("Item with this SKU already exists in your inventory.");
        }

        Item savedItem = itemRepository.save(item);
        return itemMapper.toResponseDto(savedItem);
    }

    // public List<ItemResponseDto> getAllItems() {
    //     return itemRepository.findAll()
    //             .stream()
    //             .map(itemMapper::toResponseDto)
    //             .collect(Collectors.toList());
    // }

    public Page<ItemResponseDto> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable)
            .map(itemMapper::toResponseDto);
    }
}