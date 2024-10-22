package com.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VocabularyRecommendDTO {
    private Long id;
    private String word;
    private String definition;
    private String partOfSpeech;
    private String difficultyLevel;
    private List<String> synonyms;
    private List<String> exampleSentences;
    private List<String> tags;
}
