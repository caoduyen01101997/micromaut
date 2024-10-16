package com.example.service;

import com.example.document.User;
import com.example.repository.UserRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {
    @Inject
    private UserRepository userRepository;
    public Iterable<User> list() {
        return userRepository.findAll();
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(generateId());
            return userRepository.save(user);
        } else {
            return userRepository.update(user);
        }
    }

    public Optional<User> find(@NonNull Long id) {
        return userRepository.findById(id);
    }

    private Long generateId() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Convert the UUID to a string and remove the hyphens
        String uuidStr = uuid.toString().replace("-", "");

        // Convert the first 15 characters of the UUID to a long
        Long id = Long.parseLong(uuidStr.substring(0, 10), 16);

        return id;
    }
}
