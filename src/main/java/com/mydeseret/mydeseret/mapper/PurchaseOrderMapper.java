package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.PurchaseOrderResponseDto;
import com.mydeseret.mydeseret.model.PurchaseOrder;
import com.mydeseret.mydeseret.model.PurchaseOrderItem;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class PurchaseOrderMapper {

    public PurchaseOrderResponseDto toResponseDto(PurchaseOrder po) {
        if (po == null) return null;

        PurchaseOrderResponseDto dto = new PurchaseOrderResponseDto();
        dto.setId(po.getId());
        dto.setPoNumber(po.getPoNumber());
        
        if (po.getSupplier() != null) {
            dto.setSupplierId(po.getSupplier().getId());
            dto.setVendorName(po.getSupplier().getName());
        } else {
            dto.setVendorName("Unknown Supplier");
        }
        
        dto.setStatus(po.getStatus());
        dto.setTotalCost(po.getTotalCost());
        dto.setCreatedDate(po.getCreatedDate());

        if (po.getItems() != null) {
            dto.setItems(po.getItems().stream().map(this::toItemDto).collect(Collectors.toList()));
        }
        return dto;
    }

    private PurchaseOrderResponseDto.PoItemResponseDto toItemDto(PurchaseOrderItem item) {
        PurchaseOrderResponseDto.PoItemResponseDto dto = new PurchaseOrderResponseDto.PoItemResponseDto();
        dto.setItemName(item.getItem().getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitCost(item.getUnitCost());
        return dto;
    }
}