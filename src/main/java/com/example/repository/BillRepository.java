package com.example.repository;

import com.example.document.Bill;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
//    @MongoAggregateQuery("[{$match: {$or: [{surname:{$regex: :surname}}]}}, {$sort: {surname: 1}}]")
//    List<Bill> customAggregateAndProject(String surname);
}
