package com.example.repository;

import com.example.document.Catagory;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface CatagoryRepository extends JpaRepository<Catagory,Long> {
}
