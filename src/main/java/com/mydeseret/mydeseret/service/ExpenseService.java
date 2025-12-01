package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.ExpenseRequestDto;
import com.mydeseret.mydeseret.dto.ExpenseResponseDto;
import com.mydeseret.mydeseret.mapper.ExpenseMapper;
import com.mydeseret.mydeseret.model.AmortizationSchedule;
import com.mydeseret.mydeseret.model.Expense;
import com.mydeseret.mydeseret.repository.AmortizationScheduleRepository;
import com.mydeseret.mydeseret.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private AmortizationScheduleRepository amortizationScheduleRepository;
    @Autowired private ExpenseMapper expenseMapper;

    @Transactional
    public List<ExpenseResponseDto> createExpense(ExpenseRequestDto request) {
        List<Expense> expensesToSave = new ArrayList<>();

        // IS THIS AN AMORTIZED EXPENSE?
        if (request.isAmortize() && request.getMonthsToSpread() > 1) {
            
            // Create the Master Schedule (The Audit Trail)
            AmortizationSchedule schedule = new AmortizationSchedule();
            schedule.setName(request.getDescription());
            schedule.setTotalAmount(request.getAmount());
            schedule.setTotalMonths(request.getMonthsToSpread());
            schedule.setStartDate(request.getExpenseDate());
            schedule.setEndDate(request.getExpenseDate().plusMonths(request.getMonthsToSpread() - 1));
            
            // Will need to save parent first to get in order to get ID
            schedule = amortizationScheduleRepository.save(schedule);

            //Calculate Monthly Split
            BigDecimal splitAmount = request.getAmount().divide(
                BigDecimal.valueOf(request.getMonthsToSpread()), 2, RoundingMode.HALF_UP
            );

            // Generate Children Expenses
            for (int i = 0; i < request.getMonthsToSpread(); i++) {
                Expense expense = new Expense();
                expense.setDescription(request.getDescription() + " (Month " + (i + 1) + ")");
                expense.setAmount(splitAmount);
                expense.setDate(request.getExpenseDate().plusMonths(i));
                
                //LINKs BACK TO PARENT
                expense.setAmortizationSchedule(schedule);
                
                expensesToSave.add(expense);
            }

        } else {
            // NORMAL EXPENSE (One-time)
            Expense expense = new Expense();
            expense.setDescription(request.getDescription());
            expense.setAmount(request.getAmount());
            expense.setDate(request.getExpenseDate());
            expensesToSave.add(expense);
        }

        List<Expense> savedExpenses = expenseRepository.saveAll(expensesToSave);

        return savedExpenses.stream()
                .map(expenseMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(expenseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getExpenseDate());
        
        return expenseMapper.toResponseDto(expenseRepository.save(expense));
    }

    @Transactional
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) throw new RuntimeException("Expense not found");
        expenseRepository.deleteById(id);
    }
}