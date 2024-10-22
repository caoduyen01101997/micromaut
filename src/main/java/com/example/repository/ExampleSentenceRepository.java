package com.example.repository;

import com.example.document.ExampleSentence;
import com.example.document.Vocabulary;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface ExampleSentenceRepository extends JpaRepository<ExampleSentence, Long> {
}
