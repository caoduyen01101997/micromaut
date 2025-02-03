package com.example.controller;

import java.util.Optional;

import com.example.document.Task;
import com.example.service.TaskService;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller("/task")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class TaskController {
    @Inject
    private TaskService taskService;

    @Get
    Page<Task> list(Pageable pageable) {
        return taskService.list(pageable);
    }

    @Post
    @Status(HttpStatus.CREATED)
    Task save(Task task) {
        return taskService.save(task);
    }

    @Put
    Task update(Task task) {
        return taskService.save(task);
    }

    /**
     * Get a task by ID.
     *
     * @param id the ID of the task
     * @return the task or empty if no task is found
     */
    @Get("/{id}")
    Optional<Task> find(@PathVariable Long id) {
        return taskService.find(id);
    }

    @Delete("/{id}")
    int delete(@PathVariable Long id) {
        return taskService.delete(id);
    }
}
