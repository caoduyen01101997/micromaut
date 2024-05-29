package com.example.service;

import com.example.document.Blog;
import com.example.document.User;
import com.example.repository.BlogRepository;
import com.example.repository.UserRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class BlogService {
    @Inject
    private BlogRepository blogRepository;
    public List<Blog> list() {
        return blogRepository.findAll();
    }

    public Blog save(Blog blog) {
        if (blog.getId() == null) {
            blog.setId(generateId());
            blog.setCreatedDate(new Date());
            blog.setUpdatedDate(new Date());
            return blogRepository.save(blog);
        } else {
            blog.setUpdatedDate(new Date());
            return blogRepository.update(blog);
        }
    }

    public Optional<Blog> find(@NonNull Long id) {
        return blogRepository.findById(id);
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
}
