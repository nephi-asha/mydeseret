package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.ItemRequestDto;
import com.mydeseret.mydeseret.dto.ItemResponseDto;
import com.mydeseret.mydeseret.service.ItemService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    @PreAuthorize("hasAuthority('ITEM_CREATE')")
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok(itemService.createItem(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ITEM_READ')")
    public ResponseEntity<Page<ItemResponseDto>> getItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "itemId") String sortBy
        ) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return ResponseEntity.ok(itemService.getAllItems(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ITEM_UPDATE')")
    public ResponseEntity<ItemResponseDto> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ITEM_DELETE')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}