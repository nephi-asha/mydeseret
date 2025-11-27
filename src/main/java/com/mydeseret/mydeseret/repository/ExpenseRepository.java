package com.mydeseret.mydeseret.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mydeseret.mydeseret.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

       @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
       "WHERE e.date BETWEEN :startDate AND :endDate")
       BigDecimal calculateTotalExpenses(@Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

}