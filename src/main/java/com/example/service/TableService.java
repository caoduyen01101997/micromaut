package com.example.service;

import com.example.document.Food;
import com.example.document.Table;
import com.example.repository.FoodRepository;
import com.example.repository.TableRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class TableService {
    @Inject
    private TableRepository tableRepository;
    public Iterable<Table> list() {
        return tableRepository.findAll();
    }

    public Table save(Table table) {
        if (table.getId() == null) {
            return tableRepository.save(table);
        } else {
            return tableRepository.update(table);
        }
    }

    public Optional<Table> find(@NonNull Long id) {
        return tableRepository.findById(id);
    }
}
