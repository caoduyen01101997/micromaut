package com.example.repository;

import com.example.document.Vocabulary;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface VocabularyRecommendRepository extends JpaRepository<Vocabulary, Long> {
}

