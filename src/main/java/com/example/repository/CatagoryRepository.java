package com.example.repository;

import com.example.document.Catagory;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;

@MongoRepository
public interface CatagoryRepository extends CrudRepository<Catagory,String> {
    List<Catagory> findByNameInList(String name);
}
