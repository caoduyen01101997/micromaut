package com.example.repository;

import com.example.document.BillInfo;
import io.micronaut.data.repository.CrudRepository;

public interface BillInfoRepository extends CrudRepository<BillInfo,String> {
}
