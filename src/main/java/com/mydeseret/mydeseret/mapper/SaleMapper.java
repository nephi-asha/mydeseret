package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.SaleResponseDto;
import com.mydeseret.mydeseret.model.Sale;
import com.mydeseret.mydeseret.model.SaleItem;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class SaleMapper {

    public SaleResponseDto toResponseDto(Sale sale) {
        if (sale == null) return null;

        SaleResponseDto dto = new SaleResponseDto();
        if (sale.getCustomer() != null) {
            dto.setId(sale.getCustomer().getId()); 
            dto.setCustomerName(sale.getCustomer().getName());
        } 
        else { 
            dto.setId(null); 
            dto.setCustomerName("Guest"); 
        }
        dto.setReceiptNumber(sale.getReceiptNumber());
        dto.setStatus(sale.getStatus());
        dto.setPaymentMethod(sale.getPaymentMethod());
        dto.setTotalAmount(sale.getTotalAmount());
        dto.setSaleDate(sale.getSaleDate());

        if (sale.getItems() != null) {
            dto.setItems(sale.getItems().stream()
                .map(this::toItemDto)
                .collect(Collectors.toList()));
        }
        return dto;
    }

    private SaleResponseDto.SaleItemDto toItemDto(SaleItem item) {
        SaleResponseDto.SaleItemDto dto = new SaleResponseDto.SaleItemDto();
        dto.setItemName(item.getItem().getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setSubTotal(item.getSubTotal());
        return dto;
    }
}