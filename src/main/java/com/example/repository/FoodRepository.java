package com.example.repository;

import com.example.document.Food;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
@Repository
public interface FoodRepository extends JpaRepository<Food,Long> {
}
