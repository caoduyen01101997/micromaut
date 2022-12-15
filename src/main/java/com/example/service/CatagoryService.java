package com.example.service;

import com.example.document.Catagory;
import com.example.repository.CatagoryRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.mongodb.annotation.MongoAggregateQuery;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class CatagoryService {
    @Inject
    private  CatagoryRepository fruitRepository;
    public Iterable<Catagory> list() {
        return fruitRepository.findAll();
    }

    public Catagory save(Catagory catagory) {
        if (catagory.getId() == null) {
            return fruitRepository.save(catagory);
        } else {
            return fruitRepository.update(catagory);
        }
    }

    public Optional<Catagory> find(@NonNull String id) {
        return fruitRepository.findById(id);
    }

    public Iterable<Catagory> findByNameInList(String name) {
        return fruitRepository.findByNameInList(name);
    }

}
