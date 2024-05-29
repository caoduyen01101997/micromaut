package com.example.controller;

import com.example.document.Blog;
import com.example.document.User;
import com.example.service.BlogService;
import com.example.service.UserService;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Controller("/blog")
@ExecuteOn(TaskExecutors.IO)
public class BlogController {
    @Inject
    private BlogService blogService;


    @Get
    List<Blog> list() {

        return blogService.list();
    }

    @Post
    @Status(HttpStatus.CREATED)
    Blog save(@NonNull @NotNull @Valid Blog user) {

        return blogService.save(user);
    }

    @Put
    Blog update(@NonNull @NotNull @Valid Blog user) {
        return blogService.save(user);
    }

    @Get("/{id}")
    Optional<Blog> find(@PathVariable Long id) {
        return blogService.find(id);
    }
}
