package com.example.service;

import com.example.document.BillInfo;
import com.example.repository.BillInfoRepository;
import com.example.repository.BillRepository;
import com.example.repository.CatagoryRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class BillInfoService {
    @Inject
    private BillInfoRepository billInfoRepository;
    public Iterable<BillInfo> list() {
        return billInfoRepository.findAll();
    }

    public BillInfo save(BillInfo billInfo) {
        if (billInfo.getId() == null) {
            return billInfoRepository.save(billInfo);
        } else {
            return billInfoRepository.update(billInfo);
        }
    }

    public Optional<BillInfo> find(@NonNull String id) {
        return billInfoRepository.findById(id);
    }

}
