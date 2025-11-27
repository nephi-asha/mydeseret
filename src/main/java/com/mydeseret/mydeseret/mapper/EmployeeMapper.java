package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.EmployeeResponseDto;
import com.mydeseret.mydeseret.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponseDto toResponseDto(Employee employee) {
        if (employee == null) return null;

        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setJobTitle(employee.getJobTitle());
        dto.setDepartment(employee.getDepartment());
        dto.setStatus(employee.getStatus());
        dto.setSalary(employee.getSalary());
        dto.setHireDate(employee.getHireDate());

        if (employee.getUser() != null) {
            dto.setFullName(employee.getUser().getFirstName() + " " + employee.getUser().getLastName());
            dto.setEmail(employee.getUser().getEmail());
        }
        return dto;
    }
}