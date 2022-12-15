package com.example.repository;

import com.example.document.Bill;
import io.micronaut.data.mongodb.annotation.MongoAggregateQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@MongoRepository
public interface BillRepository extends CrudRepository<Bill,String> {
//    @MongoAggregateQuery("[{$match: {$or: [{surname:{$regex: :surname}}]}}, {$sort: {surname: 1}}]")
//    List<Bill> customAggregateAndProject(String surname);
}
