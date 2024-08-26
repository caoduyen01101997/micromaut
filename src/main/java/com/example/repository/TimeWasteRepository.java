package com.example.repository;

import com.example.document.TimeWaste;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface TimeWasteRepository  extends JpaRepository<TimeWaste, Long> {

}
