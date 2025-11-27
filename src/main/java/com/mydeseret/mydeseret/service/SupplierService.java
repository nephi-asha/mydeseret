package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.SupplierRequestDto;
import com.mydeseret.mydeseret.dto.SupplierResponseDto;
import com.mydeseret.mydeseret.mapper.SupplierMapper;
import com.mydeseret.mydeseret.model.Supplier;
import com.mydeseret.mydeseret.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupplierService {

    @Autowired private SupplierRepository supplierRepository;
    @Autowired private SupplierMapper supplierMapper;

    @Transactional
    public SupplierResponseDto createSupplier(SupplierRequestDto request) {
        Supplier supplier = supplierMapper.toEntity(request);
        return supplierMapper.toResponseDto(supplierRepository.save(supplier));
    }

    public Page<SupplierResponseDto> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable)
                .map(supplierMapper::toResponseDto);
    }
}