package com.mydeseret.mydeseret.repository;

import java.util.List;
import java.util.UUID;
// import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydeseret.mydeseret.model.Employee;
import com.mydeseret.mydeseret.model.enums.EmployeeStatus;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    List<Employee> findByStatus(EmployeeStatus active);

    // Employee findByUser_UserId(UUID user_id);
    Employee findByUser_UserId(UUID userId);
}
