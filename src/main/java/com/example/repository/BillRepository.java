package com.example.repository;

import com.example.document.Bill;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.mongodb.annotation.MongoAggregateQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
//    @MongoAggregateQuery("[{$match: {$or: [{surname:{$regex: :surname}}]}}, {$sort: {surname: 1}}]")
//    List<Bill> customAggregateAndProject(String surname);
}
