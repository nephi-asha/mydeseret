package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.ItemRequestDto;
import com.mydeseret.mydeseret.dto.ItemResponseDto;
import com.mydeseret.mydeseret.service.ItemService;
import com.mydeseret.mydeseret.service.StorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/items")
@Tag(name = "Inventory Items", description = "Manage raw materials and finished goods")
@SecurityRequirement(name = "bearerAuth")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private StorageService storageService;

    @Operation(summary = "Create a new Item (JSON only)", description = "Adds a new item without an image.")
    @PostMapping
    @PreAuthorize("hasAuthority('ITEM_CREATE')")
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok(itemService.createItem(request));
    }

    @Operation(summary = "Create Item with Image", description = "Uploads an image to S3 and creates the item record. Request must be 'multipart/form-data'.")
    @PostMapping(value = "/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ITEM_CREATE')")
    public ResponseEntity<ItemResponseDto> createItemWithImage(
            @RequestPart("data") @Valid ItemRequestDto request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ItemResponseDto newItem = itemService.createItem(request);

        if (image != null && !image.isEmpty()) {
            String key = storageService.uploadFile(image, "items");
            itemService.updateItemImage(newItem.getItemId(), key);
            newItem.setImageKey(key);
            newItem.setImageUrl(storageService.getPresignedUrl(key));
        }

        return ResponseEntity.ok(newItem);
    }

    @Operation(summary = "Retrieve Items (with Search & Filtering)")
    @GetMapping
    @PreAuthorize("hasAuthority('ITEM_READ')")
    public ResponseEntity<Page<ItemResponseDto>> getItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "itemId") String sortBy,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(itemService.getAllItems(search, minPrice, maxPrice, categoryId, pageable));
    }

    @Operation(summary = "Update an Item")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ITEM_UPDATE')")
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @Operation(summary = "Delete (Deactivate) an Item")
    @DeleteMapping("/{id}")
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ITEM_DELETE')")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}