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

import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

@Service
@Audited
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

    // CLEAR CACHE ON WRITE
    // When create or update an item is implemented, the old list is stale. Delete it.
    @Transactional
    @CacheEvict(value = "items", allEntries = true)
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

    // Key structure: "items::page_0_size_20_sort_id"
    // @Cacheable(value = "items", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")
    // public Page<ItemResponseDto> getAllItems(Pageable pageable) {
    //     return itemRepository.findAll(pageable)
    //         .map(itemMapper::toResponseDto);
    // }

    // @Transactional
    // public ItemResponseDto updateItem(Long id, ItemRequestDto request) {
    //     Item item = itemRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Item not found"));

    //     item.setName(request.getName());
    //     item.setDescription(request.getDescription());
    //     item.setUnitOfMeasure(request.getUnitOfMeasure());
    //     item.setCostPrice(request.getCostPrice());
    //     item.setSellingPrice(request.getSellingPrice());
    //     item.setReorderPoint(request.getReorderPoint());

    //     // If category changed
    //     if (request.getCategoryId() != null) {
    //          Category category = categoryRepository.findById(request.getCategoryId())
    //                 .orElseThrow(() -> new RuntimeException("Category not found"));
    //          item.setCategory(category);
    //     }

    //     return itemMapper.toResponseDto(itemRepository.save(item));
    // }

    // @Transactional
    // public void deleteItem(Long id) {
    //     if (!itemRepository.existsById(id)) {
    //         throw new RuntimeException("Item not found");
    //     }
    //     itemRepository.deleteById(id);
    // }

    @Transactional
    public ItemResponseDto updateItem(Long id, ItemRequestDto request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (request.getName() != null) item.setName(request.getName());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getUnitOfMeasure() != null) item.setUnitOfMeasure(request.getUnitOfMeasure());
        if (request.getCostPrice() != null) item.setCostPrice(request.getCostPrice());
        if (request.getSellingPrice() != null) item.setSellingPrice(request.getSellingPrice());
        if (request.getReorderPoint() > 0) item.setReorderPoint(request.getReorderPoint());

        if (request.getCategoryId() != null) {
             Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
             item.setCategory(category);
        }

        return itemMapper.toResponseDto(itemRepository.save(item));
    }

    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        // It needs to first Check if it's safe to deactivate.
        if (item.getQuantityOnHand() > 0) {
            throw new RuntimeException("Cannot delete Item with existing stock. Please adjust stock to 0 first.");
        }
        
        // Soft Delete
        item.setActive(false); 
        itemRepository.save(item);
    }

    // Key structure: "items::page_0_size_20_sort_id"
    @Cacheable(value = "items", key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort")    
    public Page<ItemResponseDto> getAllItems(Pageable pageable) {
        return itemRepository.findByActiveTrue(pageable) 
                .map(itemMapper::toResponseDto);
    }
}