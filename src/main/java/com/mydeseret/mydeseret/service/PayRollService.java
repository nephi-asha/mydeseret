package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.model.*;
import com.mydeseret.mydeseret.model.enums.EmployeeStatus;
import com.mydeseret.mydeseret.model.enums.PayRollStatus;
import com.mydeseret.mydeseret.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PayRollService {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private PayRollRepository payrollRepository;
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private UserRepository userRepository;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional
    public String generateMonthlyPayroll() {
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);

        List<Employee> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);

        int count = 0;
        for (Employee emp : activeEmployees) {
            boolean exists = payrollRepository.existsByEmployeeAndPayPeriod(emp, thisMonth);
            if (!exists) {
                PayRoll payroll = new PayRoll();
                payroll.setEmployee(emp);
                payroll.setPayPeriod(thisMonth);
                payroll.setBaseSalary(emp.getSalary());
                payroll.setNetPay(emp.getSalary());
                payroll.setStatus(PayRollStatus.PENDING);
                
                payrollRepository.save(payroll);
                count++;
            }
        }
        return "Generated payroll for " + count + " employees.";
    }

    @Transactional
    public PayRoll processPayment(Long payrollId) {
        PayRoll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll record not found"));

        if (payroll.getStatus() == PayRollStatus.PAID) {
            throw new RuntimeException("Employee already paid for this month.");
        }

        payroll.setStatus(PayRollStatus.PAID);
        payroll.setPaymentDate(LocalDate.now());
        
        BigDecimal net = payroll.getBaseSalary().add(payroll.getBonuses()).subtract(payroll.getDeductions());
        payroll.setNetPay(net);

        payrollRepository.save(payroll);

        Expense expense = new Expense();
        expense.setAmount(net);
        expense.setDescription("Payroll: " + payroll.getEmployee().getUser().getFirstName() + " - " + payroll.getPayPeriod());
        expense.setDate(LocalDate.now());
        expense.setCategory("Salaries");
        
        expenseRepository.save(expense);

        return payroll;
    }
}