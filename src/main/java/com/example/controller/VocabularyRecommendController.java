package com.example.controller;

import com.example.DTO.VocabularyRecommendDTO;
import com.example.document.Vocabulary;
import com.example.service.VocabularyRecommendService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@Controller("/vocabulary")
@Secured(SecurityRule.IS_ANONYMOUS)
public class VocabularyRecommendController {

    @Inject
    private VocabularyRecommendService vocabularyRecommendService;

    @Post
    public HttpResponse<Vocabulary> createVocabulary(@Body VocabularyRecommendDTO dto) {
        Vocabulary createdVocabulary = vocabularyRecommendService.createVocabulary(dto);
        return HttpResponse.created(createdVocabulary);
    }

    @Get
    public List<Vocabulary> getAllVocabulary() {
        return vocabularyRecommendService.getAllVocabulary();
    }

    @Get("/{id}")
    public HttpResponse<Vocabulary> getVocabularyById(@PathVariable Long id) {
        Optional<Vocabulary> Vocabulary = vocabularyRecommendService.getVocabularyById(id);
        return Vocabulary.map(HttpResponse::ok).orElseGet(() -> HttpResponse.notFound());
    }

    @Put("/{id}")
    public HttpResponse<Vocabulary> updateVocabulary(@PathVariable Long id, @Body VocabularyRecommendDTO dto) {
        Vocabulary updatedVocabulary = vocabularyRecommendService.updateVocabulary(id, dto);
        return HttpResponse.ok(updatedVocabulary);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deleteVocabulary(@PathVariable Long id) {
        vocabularyRecommendService.deleteVocabulary(id);
        return HttpResponse.noContent();
    }
}
