package com.example.repository;

import com.example.document.BillInfo;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.repository.CrudRepository;
@Repository
public interface BillInfoRepository extends JpaRepository<BillInfo,Long> {
}
