package com.example.service;

import com.example.document.Bill;
import com.example.repository.BillRepository;
import jakarta.inject.Singleton;

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
}
