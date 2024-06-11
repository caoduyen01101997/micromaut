package com.example.repository;

import com.example.document.TimeWaste;
import io.micronaut.data.jpa.repository.JpaRepository;

public interface TimeWasteRepository  extends JpaRepository<TimeWaste, Long> {

}
