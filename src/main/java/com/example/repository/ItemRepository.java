package com.example.repository;
import com.example.document.Item;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

@Repository
public interface ItemRepository extends PageableRepository<Item, Long> {
    // @Query("select i from item i")
    Page<Item> findAll(Pageable pageable);
}
