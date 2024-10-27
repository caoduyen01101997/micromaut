package com.example.repository;

import java.util.List;

import com.example.DTO.VocabularyRecommendDTO;
import com.example.document.Vocabulary;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface VocabularyRecommendRepository extends JpaRepository<Vocabulary, Long> {

    @Query("SELECT v FROM vocabulary_word v ORDER BY v.id DESC")
    List<Vocabulary> getAllVocabulary();

}

