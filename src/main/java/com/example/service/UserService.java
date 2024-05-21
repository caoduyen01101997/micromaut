package com.example.service;

import com.example.document.User;
import com.example.repository.UserRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class UserService {
    @Inject
    private UserRepository userRepository;
    public Iterable<User> list() {
        return userRepository.findAll();
    }

    public User save(User user) {
        if (user.getId() == null) {
            return userRepository.save(user);
        } else {
            return userRepository.update(user);
        }
    }

    public Optional<User> find(@NonNull Long id) {
        return userRepository.findById(id);
    }
}
