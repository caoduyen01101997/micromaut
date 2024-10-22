package com.example.repository;

import com.example.document.Tag;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
