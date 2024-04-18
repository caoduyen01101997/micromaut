package com.example.repository;

import com.example.document.Catagory;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;

@Repository
public interface CatagoryRepository extends JpaRepository<Catagory,Long> {
    List<Catagory> findByNameInList(String name);
}
