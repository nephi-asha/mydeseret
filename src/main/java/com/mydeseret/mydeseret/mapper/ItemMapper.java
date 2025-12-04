package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.ItemRequestDto;
import com.mydeseret.mydeseret.dto.ItemResponseDto;
import com.mydeseret.mydeseret.model.Item;
import com.mydeseret.mydeseret.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    @Autowired
    private StorageService storageService;

    public Item toEntity(ItemRequestDto dto) {
        if (dto == null) return null;

        Item item = new Item();
        item.setName(dto.getName());
        item.setSku(dto.getSku());
        item.setDescription(dto.getDescription());
        item.setUnitOfMeasure(dto.getUnitOfMeasure());
        item.setCostPrice(dto.getCostPrice());
        item.setSellingPrice(dto.getSellingPrice());
        item.setReorderPoint(dto.getReorderPoint());
        
        return item;
    }

    public ItemResponseDto toResponseDto(Item item) {
        if (item == null) return null;

        ItemResponseDto dto = new ItemResponseDto();
        dto.setItemId(item.getItemId());
        dto.setName(item.getName());
        dto.setSku(item.getSku());
        dto.setDescription(item.getDescription());
        dto.setUnitOfMeasure(item.getUnitOfMeasure());
        dto.setCostPrice(item.getCostPrice());
        dto.setSellingPrice(item.getSellingPrice());
        dto.setQuantityOnHand(item.getQuantityOnHand());

        if (item.getCategory() != null) {
            dto.setCategoryId(item.getCategory().getCategoryId());
            dto.setCategoryName(item.getCategory().getName());
        }

        if (item.getImageKey() != null) {
            dto.setImageKey(item.getImageKey());
            // Generate a temporary public URL (valid for 60 mins)
            dto.setImageUrl(storageService.getPresignedUrl(item.getImageKey()));
        }
        
        return dto;
    }
}