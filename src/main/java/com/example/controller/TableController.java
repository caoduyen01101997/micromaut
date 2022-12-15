package com.example.controller;

import com.example.document.Table;
import com.example.service.TableService;
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

@Controller("/tables")
@ExecuteOn(TaskExecutors.IO)
public class TableController {
    @Inject
    private  TableService tableService;


    @Get
    Iterable<Table> list() {

        return tableService.list();
    }

    @Post
    @Status(HttpStatus.CREATED)
    Table save(@NonNull @NotNull @Valid Table table) {

        return tableService.save(table);
    }

    @Put
    Table update(@NonNull @NotNull @Valid Table table) {
        return tableService.save(table);
    }

    @Get("/{id}")
    Optional<Table> find(@PathVariable String id) {
        return tableService.find(id);
    }

}
