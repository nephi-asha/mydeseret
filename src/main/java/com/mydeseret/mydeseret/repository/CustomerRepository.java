package com.mydeseret.mydeseret.repository;

import com.mydeseret.mydeseret.model.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByActiveTrue(Pageable pageable);
}