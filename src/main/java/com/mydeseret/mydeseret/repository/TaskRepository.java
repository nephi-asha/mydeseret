package com.mydeseret.mydeseret.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydeseret.mydeseret.model.Employee;
import com.mydeseret.mydeseret.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

    List<Task> findByAssignee(Employee me);
    
}
