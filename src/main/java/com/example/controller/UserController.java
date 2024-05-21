package com.example.controller;

import com.example.document.User;
import com.example.service.UserService;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Controller("/account")
@ExecuteOn(TaskExecutors.IO)
public class UserController {
    @Inject
    private UserService userService;


    @Get
    Iterable<User> list() {

        return userService.list();
    }

    @Post
    @Status(HttpStatus.CREATED)
    User save(@NonNull @NotNull @Valid User user) {

        return userService.save(user);
    }

    @Put
    User update(@NonNull @NotNull @Valid User user) {
        return userService.save(user);
    }

    @Get("/{id}")
    Optional<User> find(@PathVariable Long id) {
        return userService.find(id);
    }
}
