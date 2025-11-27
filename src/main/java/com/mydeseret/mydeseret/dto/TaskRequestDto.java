package com.mydeseret.mydeseret.dto;

import com.mydeseret.mydeseret.model.enums.TaskPriority;
import jakarta.validation.constraints.NotNull;
// import lombok.Data;
import java.time.LocalDate;

// @Data
public class TaskRequestDto {
    @NotNull private String title;
    private String description;
    @NotNull private TaskPriority priority;
    @NotNull private LocalDate dueDate;
    @NotNull private Long assigneeEmployeeId;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public TaskPriority getPriority() {
        return priority;
    }
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public Long getAssigneeEmployeeId() {
        return assigneeEmployeeId;
    }
    public void setAssigneeEmployeeId(Long assigneeEmployeeId) {
        this.assigneeEmployeeId = assigneeEmployeeId;
    }

    
}