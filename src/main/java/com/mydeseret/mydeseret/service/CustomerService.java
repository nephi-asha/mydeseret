package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.CustomerRequestDto;
import com.mydeseret.mydeseret.dto.CustomerResponseDto;
import com.mydeseret.mydeseret.mapper.CustomerMapper;
import com.mydeseret.mydeseret.model.Customer;
import com.mydeseret.mydeseret.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private CustomerMapper customerMapper;

    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto request) {
        Customer customer = customerMapper.toEntity(request);
        return customerMapper.toResponseDto(customerRepository.save(customer));
    }

    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponseDto);
    }
}