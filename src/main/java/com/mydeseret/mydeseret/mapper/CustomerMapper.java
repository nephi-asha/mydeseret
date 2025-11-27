package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.CustomerRequestDto;
import com.mydeseret.mydeseret.dto.CustomerResponseDto;
import com.mydeseret.mydeseret.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDto dto) {
        if (dto == null) return null;
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        if (dto.getCreditLimit() != null) {
            customer.setCreditLimit(dto.getCreditLimit());
        }
        return customer;
    }

    public CustomerResponseDto toResponseDto(Customer customer) {
        if (customer == null) return null;
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setCurrentDebt(customer.getCurrentDebt());
        dto.setCreditLimit(customer.getCreditLimit());
        return dto;
    }
}