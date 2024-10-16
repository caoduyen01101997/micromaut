package com.example.controller;

import com.example.document.User;
import com.example.service.UserService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import javax.validation.Valid;
import java.util.Optional;

@Controller("/account")
@Secured(SecurityRule.IS_ANONYMOUS)
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
    User save( User user) {

        return userService.save(user);
    }

    @Put
    User update( User user) {
        return userService.save(user);
    }

    @Get("/{id}")
    Optional<User> find(@PathVariable Long id) {
        return userService.find(id);
    }
}
