package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.ExpenseResponseDto;
import com.mydeseret.mydeseret.model.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public ExpenseResponseDto toResponseDto(Expense expense) {
        if (expense == null) return null;

        ExpenseResponseDto dto = new ExpenseResponseDto();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        
        if (expense.getPurchaseOrder() != null) {
            dto.setRelatedPoNumber(expense.getPurchaseOrder().getPoNumber());
        }
        return dto;
    }
}