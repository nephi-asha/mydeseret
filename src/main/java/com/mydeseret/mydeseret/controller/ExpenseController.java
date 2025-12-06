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
@io.swagger.v3.oas.annotations.tags.Tag(name = "Expenses", description = "Track business expenses")
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_UPDATE')")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long id,
            @Valid @RequestBody ExpenseRequestDto request) {
        return ResponseEntity.ok(expenseService.updateExpense(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EXPENSE_DELETE')")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}