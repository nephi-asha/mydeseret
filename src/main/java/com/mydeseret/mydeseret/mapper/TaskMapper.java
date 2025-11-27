package com.mydeseret.mydeseret.mapper;

import com.mydeseret.mydeseret.dto.TaskResponseDto;
import com.mydeseret.mydeseret.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponseDto toResponseDto(Task task) {
        if (task == null) return null;

        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());

        if (task.getAssignee() != null && task.getAssignee().getUser() != null) {
            dto.setAssigneeName(task.getAssignee().getUser().getFirstName());
        }
        if (task.getReporter() != null) {
            dto.setReporterName(task.getReporter().getFirstName());
        }
        return dto;
    }
}