package com.example.repository;

import com.example.document.Synonym;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface SynonymRepository extends JpaRepository<Synonym, Long> {
}
