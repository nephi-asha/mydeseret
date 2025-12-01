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

    @Transactional
    public SupplierResponseDto updateSupplier(Long id, SupplierRequestDto request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());

        return supplierMapper.toResponseDto(supplierRepository.save(supplier));
    }

    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) throw new RuntimeException("Supplier not found");
        supplierRepository.deleteById(id);
    }
}