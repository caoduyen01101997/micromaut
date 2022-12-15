package com.example.service;

import com.example.document.Bill;
import com.example.repository.BillRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton

public class BillService {
    private BillRepository billRepository;
    public Iterable<Bill> list() {
        return billRepository.findAll();
    }

    public Bill save(Bill bill) {
        if (bill.getId() == null) {
            return billRepository.save(bill);
        } else {
            return billRepository.update(bill);
        }
    }

    public Optional<Bill> find(@NonNull String id) {
        return billRepository.findById(id);
    }
}
