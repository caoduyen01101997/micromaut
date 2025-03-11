package com.example.repository;
import java.util.List;

import com.example.document.Item;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

@Repository
public interface ItemRepository extends PageableRepository<Item, Long> {
    Page<Item> findAll(Pageable pageable);

    @Query("SELECT i FROM Item i WHERE (:name = '' OR i.name LIKE CONCAT('%',:name,'%')) AND (:priceSell = 0.0 OR i.priceSell = :priceSell) AND (:priceBuy = 0.0 OR i.priceBuy = :priceBuy)")
    List<Item> findByNameAndPriceSellAndPriceBuy(String name, Double priceSell, Double priceBuy);
}
