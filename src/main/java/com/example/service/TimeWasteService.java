package com.example.service;

import com.example.document.TimeWaste;
import com.example.repository.TimeWasteRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class TimeWasteService {

    @Inject
    private TimeWasteRepository timeWasteRepository;
    public Page<TimeWaste> list(Pageable pageable) {
        return timeWasteRepository.findAll(pageable);
    }

    public TimeWaste save(TimeWaste blog) {
        if (blog.getId() == null) {
            blog.setId(generateId());
            return timeWasteRepository.save(blog);
        } else {
            blog.setEndTime(new Date());
            return timeWasteRepository.update(blog);
        }
    }

    public Optional<TimeWaste> find(@NonNull Long id) {
        return timeWasteRepository.findById(id);
    }

    private Long generateId() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Convert the UUID to a string and remove the hyphens
        String uuidStr = uuid.toString().replace("-", "");

        // Convert the first 15 characters of the UUID to a long
        Long id = Long.parseLong(uuidStr.substring(0, 15), 16);

        return id;
    }

    public int  delete(Long id) {
        timeWasteRepository.deleteById(id);

        Optional<TimeWaste> blog = timeWasteRepository.findById(id);
        if (blog.isEmpty()) {
            return 1;
        } else {
            return 0;
        }
    }
}
