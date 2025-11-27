package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.ExpenseRequestDto;
import com.mydeseret.mydeseret.dto.ExpenseResponseDto;
import com.mydeseret.mydeseret.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    @PreAuthorize("hasAuthority('EXPENSE_CREATE')")
    public ResponseEntity<List<ExpenseResponseDto>> createExpense(@Valid @RequestBody ExpenseRequestDto request) {
        return ResponseEntity.ok(expenseService.createExpense(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EXPENSE_READ')")
    public ResponseEntity<List<ExpenseResponseDto>> getExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }
}