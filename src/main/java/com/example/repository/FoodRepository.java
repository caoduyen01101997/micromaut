package com.example.repository;

import com.example.document.Food;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;
@MongoRepository
public interface FoodRepository extends CrudRepository<Food,String> {
}
