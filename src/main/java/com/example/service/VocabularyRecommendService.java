package com.example.service;
import com.example.DTO.VocabularyRecommendDTO;
import com.example.document.ExampleSentence;
import com.example.document.Synonym;
import com.example.document.Tag;
import com.example.document.Vocabulary;
import com.example.repository.ExampleSentenceRepository;
import com.example.repository.SynonymRepository;
import com.example.repository.TagRepository;
import com.example.repository.VocabularyRecommendRepository;

import io.micronaut.transaction.annotation.TransactionalEventListener;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;

@Singleton
public class VocabularyRecommendService {

    @Inject
    private  TagRepository tagRepository;
    @Inject
    private SynonymRepository synonymRepository;
    @Inject
    private ExampleSentenceRepository exampleSentenceRepository;
    @Inject
    private VocabularyRecommendRepository vocabularyRecommendRepository;


//    public Vocabulary createVocabulary(VocabularyRecommendDTO dto) {
//        return transactionManager.executeWrite(status -> {
//            try {
//                // Create the Vocabulary entity
//                Vocabulary vocabularyRecommend = new Vocabulary();
//                vocabularyRecommend.setWord(dto.getWord());
//                vocabularyRecommend.setDefinition(dto.getDefinition());
//                vocabularyRecommend.setPartOfSpeech(dto.getPartOfSpeech());
//                vocabularyRecommend.setDifficultyLevel(dto.getDifficultyLevel());
//
//                // Add synonyms
//                for (String synonymWord : dto.getSynonyms()) {
//                    Synonym synonym = new Synonym();
//                    synonym.setSynonym(synonymWord);
//                    synonym.setVocabularyWord(vocabularyRecommend);
//                    vocabularyRecommend.getSynonyms().add(synonym);
//                }
//
//                // Add example sentences
//                for (String sentenceText : dto.getExampleSentences()) {
//                    ExampleSentence sentence = new ExampleSentence();
//                    sentence.setSentence(sentenceText);
//                    sentence.setVocabularyWord(vocabularyRecommend);
//
//                    vocabularyRecommend.getExampleSentences().add(sentence);
//                }
//
//                // Add tags
//                for (String tagName : dto.getTags()) {
//                    Tag tag = new Tag();
//                    tag.setTag(tagName);
//                    tag.setVocabularyWord(vocabularyRecommend);
//                    vocabularyRecommend.getTags().add(tag);
//                }
//
//                // Save the Vocabulary entity (this will also save related entities due to cascading)
//                Vocabulary savedVocabulary = vocabularyRecommendRepository.save(vocabularyRecommend);
//
//                // If everything is successful, return the saved vocabulary
//                return savedVocabulary;
//            } catch (Exception e) {
//                // If an exception occurs, mark the transaction for rollback
//                status.setRollbackOnly();
//                throw e;
//            }
//        });
//    }
public Vocabulary createVocabulary(VocabularyRecommendDTO dto) {
    Vocabulary vocabularyRecommend = new Vocabulary();
    vocabularyRecommend.setWord(dto.getWord());
    vocabularyRecommend.setDefinition(dto.getDefinition());
    vocabularyRecommend.setPartOfSpeech(dto.getPartOfSpeech());
    vocabularyRecommend.setDifficultyLevel(dto.getDifficultyLevel());

    // Add synonyms
    for (String synonymWord : dto.getSynonyms()) {
        Synonym synonym = new Synonym();
        synonym.setSynonym(synonymWord);
        vocabularyRecommend.getSynonyms().add(synonym);
    }

    // Add example sentences
    for (String sentenceText : dto.getExampleSentences()) {
        ExampleSentence sentence = new ExampleSentence();
        sentence.setSentence(sentenceText);
        vocabularyRecommend.getExampleSentences().add(sentence);
    }

    // Add tags
    for (String tagName : dto.getTags()) {
        Tag tag = new Tag();
        tag.setTag(tagName);
        vocabularyRecommend.getTags().add(tag);
    }

    // Save the Vocabulary entity (this will also save related entities due to cascading)
    Vocabulary savedVocabulary = vocabularyRecommendRepository.save(vocabularyRecommend);

    // If everything is successful, return the saved vocabulary
    return savedVocabulary;
}

    @Transactional
    public List<VocabularyRecommendDTO> getAllVocabulary() {
        List<VocabularyRecommendDTO> dtos = new ArrayList<>();
        for (Vocabulary vocabulary : vocabularyRecommendRepository.getAllVocabulary()) {
            VocabularyRecommendDTO dto = new VocabularyRecommendDTO();
            // Hibernate.initialize(vocabulary.getSynonyms());
            // Hibernate.initialize(vocabulary.getTags());
            // Hibernate.initialize(vocabulary.getExampleSentences());
            dto.setId(vocabulary.getId());
            dto.setWord(vocabulary.getWord());
            dto.setDefinition(vocabulary.getDefinition());
            dto.setPartOfSpeech(vocabulary.getPartOfSpeech());
            dto.setDifficultyLevel(vocabulary.getDifficultyLevel());
            dto.setSynonyms(vocabulary.getSynonyms().stream().map(Synonym::getSynonym).collect(Collectors.toList()));
            dto.setExampleSentences(vocabulary.getExampleSentences().stream().map(ExampleSentence::getSentence).collect(Collectors.toList()));
            dto.setTags(vocabulary.getTags().stream().map(Tag::getTag).collect(Collectors.toList()));
            dtos.add(dto);
        }
        return dtos;
    }

    public Optional<Vocabulary> getVocabularyById(Long id) {
        return vocabularyRecommendRepository.findById(id);
    }

    public Vocabulary updateVocabulary(Long id, VocabularyRecommendDTO dto) {
        Optional<Vocabulary> vocabularyOptional = vocabularyRecommendRepository.findById(id);
        if (vocabularyOptional.isPresent()) {
            Vocabulary vocabularyRecommend = vocabularyOptional.get();
            // Update the entity
            vocabularyRecommend.setWord(dto.getWord());
            vocabularyRecommend.setDefinition(dto.getDefinition());
            vocabularyRecommend.setPartOfSpeech(dto.getPartOfSpeech());
            vocabularyRecommend.setDifficultyLevel(dto.getDifficultyLevel());
            // Update synonyms, example sentences, and tags as needed.
            return vocabularyRecommendRepository.save(vocabularyRecommend);
        } else {
            throw new RuntimeException("Vocabulary not found");
        }
    }

    public void deleteVocabulary(Long id) {
        vocabularyRecommendRepository.deleteById(id);
    }
}
