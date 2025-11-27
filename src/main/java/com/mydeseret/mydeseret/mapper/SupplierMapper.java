package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.SupplierRequestDto;
import com.mydeseret.mydeseret.dto.SupplierResponseDto;
import com.mydeseret.mydeseret.model.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public Supplier toEntity(SupplierRequestDto dto) {
        if (dto == null) return null;
        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());
        return supplier;
    }

    public SupplierResponseDto toResponseDto(Supplier supplier) {
        if (supplier == null) return null;
        SupplierResponseDto dto = new SupplierResponseDto();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        return dto;
    }
}