package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.TaskRequestDto;
import com.mydeseret.mydeseret.model.*;
import com.mydeseret.mydeseret.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

// import java.util.List;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private UserRepository userRepository;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    public Task assignTask(TaskRequestDto request) {
        User manager = getAuthenticatedUser();
        
        Employee assignee = employeeRepository.findById(request.getAssigneeEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setAssignee(assignee);
        task.setReporter(manager);

        return taskRepository.save(task);
    }

    // public List<Task> getMyTasks() {
    //     User user = getAuthenticatedUser();
    //     Employee me = employeeRepository.findByUser_UserId(user.getUserId());

    //     return taskRepository.findByAssignee(me);
    // }
}