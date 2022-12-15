package com.example.repository;

import com.example.document.Table;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

@MongoRepository
public interface TableRepository extends CrudRepository<Table, String> {
}
