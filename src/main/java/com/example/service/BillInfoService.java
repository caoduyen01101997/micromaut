package com.example.service;

import com.example.document.BillInfo;
import com.example.repository.BillInfoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

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


}
