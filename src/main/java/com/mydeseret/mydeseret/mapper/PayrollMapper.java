package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.PayrollResponseDto;
import com.mydeseret.mydeseret.model.PayRoll;
import org.springframework.stereotype.Component;

@Component
public class PayrollMapper {

    public PayrollResponseDto toResponseDto(PayRoll payroll) {
        if (payroll == null) return null;

        PayrollResponseDto dto = new PayrollResponseDto();
        dto.setId(payroll.getId());
        dto.setPayPeriod(payroll.getPayPeriod());
        dto.setBaseSalary(payroll.getBaseSalary());
        dto.setNetPay(payroll.getNetPay());
        dto.setStatus(payroll.getStatus());
        dto.setPaymentDate(payroll.getPaymentDate());

        if (payroll.getEmployee() != null && payroll.getEmployee().getUser() != null) {
            String name = payroll.getEmployee().getUser().getFirstName() + " " + 
                          payroll.getEmployee().getUser().getLastName();
            dto.setEmployeeName(name);
        }
        return dto;
    }
}