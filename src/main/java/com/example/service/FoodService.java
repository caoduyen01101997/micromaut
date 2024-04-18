package com.example.service;

import com.example.document.BillInfo;
import com.example.document.Food;
import com.example.repository.BillInfoRepository;
import com.example.repository.FoodRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton

public class FoodService {
    @Inject
    private FoodRepository foodRepository;
    public Iterable<Food> list() {
        return foodRepository.findAll();
    }

    public Food save(Food billInfo) {
        if (billInfo.getId() == null) {
            return foodRepository.save(billInfo);
        } else {
            return foodRepository.update(billInfo);
        }
    }

    public Optional<Food> find(@NonNull Long id) {
        return foodRepository.findById(id);
    }
}
