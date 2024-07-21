package com.example.repository;

import com.example.document.Task;
import io.micronaut.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
}
