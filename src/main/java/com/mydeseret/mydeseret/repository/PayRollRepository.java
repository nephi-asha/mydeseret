package com.mydeseret.mydeseret.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydeseret.mydeseret.model.Employee;
import com.mydeseret.mydeseret.model.PayRoll;

public interface PayRollRepository extends JpaRepository<PayRoll, Long>{

    boolean existsByEmployeeAndPayPeriod(Employee emp, LocalDate thisMonth);
}
