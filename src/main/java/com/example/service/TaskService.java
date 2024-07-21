package com.example.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.example.document.Task;
import com.example.repository.TaskRepository;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TaskService {
    @Inject
    private TaskRepository taskRepository;

    public Page<Task> list(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(generateId());
            task.setCreatedDate(new Date());
            return taskRepository.save(task);
        } else {
            task.setReminderDate(new Date());
            task.setResolveDate(new Date());
            return taskRepository.update(task);
        }
    }

    public Optional<Task> find(@NonNull Long id) {
        return taskRepository.findById(id);
    }

    private Long generateId() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Convert the UUID to a string and remove the hyphens
        String uuidStr = uuid.toString().replace("-", "");

        // Convert the first 15 characters of the UUID to a long
        Long id = Long.parseLong(uuidStr.substring(0, 15), 16);

        return id;
    }

    public int  delete(Long id) {
        taskRepository.deleteById(id);

        Optional<Task> blog = taskRepository.findById(id);
        if (blog.isEmpty()) {
            return 1;
        } else {
            return 0;
        }
    }
}
