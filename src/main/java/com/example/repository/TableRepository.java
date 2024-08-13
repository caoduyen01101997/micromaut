package com.example.repository;

import com.example.document.Table;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
}
