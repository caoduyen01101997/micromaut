package com.example.repository;

import com.example.document.CartItem;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
