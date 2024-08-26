package com.example.controller;

import java.util.Optional;

import com.example.document.TimeWaste;
import com.example.service.TimeWasteService;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller("/time-waste")
@Secured(SecurityRule.IS_ANONYMOUS)
@ExecuteOn(TaskExecutors.IO)
public class TimeWasteController {
     @Inject
    private TimeWasteService timeWasteService;


    @Get
    Page<TimeWaste> list(Pageable pageable) {
        return timeWasteService.list(pageable);
    }

    @Post
    @Status(HttpStatus.CREATED)
    TimeWaste save(TimeWaste timeWaste) {
        return timeWasteService.save(timeWaste);
    }

    @Put
    TimeWaste update(TimeWaste user) {
        return timeWasteService.save(user);
    }

    @Get("/{id}")
    Optional<TimeWaste> find(@PathVariable Long id) {
        return timeWasteService.find(id);
    }
    
}
