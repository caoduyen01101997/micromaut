package com.example.repository;

import com.example.document.Table;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
}
